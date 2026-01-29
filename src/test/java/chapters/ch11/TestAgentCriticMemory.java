package chapters.ch11;

import chapters.ch11.domain.agent.memory.CriticMemoryLunar;
import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.factory.LunarAgentParamsFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import chapters.ch11.helper.RadialBasisAdapter;
import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.TrainDataOld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestAgentCriticMemory {


    public static final double TOL = 0.9;
    public static final int N_FITS = 100;
    public static final double LEARNING_RATE = 0.9;
    LunarParameters lunarParameters;
    CriticMemoryLunar memory;
    AgentParameters agentParameters;

    @BeforeEach
    void init() {
        lunarParameters = LunarEnvParamsFactory.produceDefault();
        agentParameters = LunarAgentParamsFactory.newDefault(lunarParameters)
                .withLearningRateCritic(LEARNING_RATE)
                .withNFits(1);
        memory = CriticMemoryLunar.zeroWeights(agentParameters, lunarParameters);
    }

    @Test
    void testCreateMemory() {
        var criticMemoryLunar = CriticMemoryLunar.zeroWeights(agentParameters, lunarParameters);
        var rb = criticMemoryLunar.getMemory();
        assertEquals(agentParameters.nKernelsY() * agentParameters.nKernelsSpeed(), rb.getWeights().size());
    }

    @Test
    void testReadAnyState() {
        for (int i = 0; i < 10; i++) {
            StateLunar state = StateLunar.randomPosAndZeroSpeed(lunarParameters);
            assertEquals(0.0, memory.read(state));
        }
    }

    @Test
    void testFit_oneState() {
        StateLunar state = StateLunar.of(0.5, 0.5);
        int vTarget = 10;
        fitMemory(memory, state, vTarget);
        assertEquals(vTarget, memory.read(state), TOL);
    }

    @Test
    void testFit_twoDifferentYpos() {
        var states = List.of(StateLunar.of(5, 0), StateLunar.of(1, 0));
        var vTargets = List.of(10d, 1d);
        fitStatesToTargets(states, vTargets, memory);
        assertStatesAndTargets(states, vTargets, memory);
    }

    @Test
    void testFit_twoDifferentSpeeds() {
        var states = List.of(StateLunar.of(5, 0), StateLunar.of(5, lunarParameters.spdMax()));
        var vTargets = List.of(10d, 1d);
        fitStatesToTargets(states, vTargets, memory);
        assertStatesAndTargets(states, vTargets, memory);
    }

    @Test
    void testFit_AllStatesToTarget5() {
        double vTarget = 5;
        for (int i = 0; i < 500; i++) {
            StateLunar state = StateLunar.randomPosAndSpeed(lunarParameters);
            fitMemory(memory, state, vTarget);
        }

        var states = List.of(StateLunar.of(5, 0), StateLunar.of(1, 0));
        var vTargets = List.of(vTarget,vTarget);
        assertStatesAndTargets(states, vTargets, memory);
    }


    private static void assertStatesAndTargets(
            List<StateLunar> states, List<Double> vTargets, CriticMemoryLunar criticMemoryLunar) {
        for (StateLunar state : states) {
            double vTarget = vTargets.get(states.indexOf(state));
            assertEquals(vTarget, criticMemoryLunar.read(state), TOL);
        }
    }

    private static void fitStatesToTargets(
            List<StateLunar> states, List<Double> vTargets, CriticMemoryLunar criticMemoryLunar) {
        for (StateLunar state : states) {
            double vTarget = vTargets.get(states.indexOf(state));
            fitMemory(criticMemoryLunar, state, vTarget);
        }
    }


    private static void fitMemory(CriticMemoryLunar criticMemoryLunar, StateLunar state, double vTarget) {
        var data = TrainData.empty();
        for (int i = 0; i < N_FITS; i++) {
            data.clear();
            double error = vTarget - criticMemoryLunar.read(state);
            var in = RadialBasisAdapter.asInput(state);
            data.addListIn(in, error);
            //criticMemoryLunar.fit(data);
            criticMemoryLunar.fitFromError(data);
        }
    }


}
