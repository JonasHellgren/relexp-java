package chapters.ch11;

import chapters.ch11.domain.agent.memory.ActorMemoryLunar;
import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.factory.LunarAgentParamsFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import chapters.ch11.helper.RadialBasisAdapter;
import core.foundation.gadget.training.TrainDataOld;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestAgentActorMemory {

    public static final double INIT_WEIGHT_LOG_STD = 0.25;
    private ActorMemoryLunar actorMemoryLunar, actorMemoryLunarNonZeroForStd;
    private LunarParameters lunarParameters;
    private AgentParameters agentParameters;

    @BeforeEach
    void setup() {
        lunarParameters = LunarEnvParamsFactory.produceDefault();
        agentParameters = LunarAgentParamsFactory.newDefault(lunarParameters);
        actorMemoryLunar = ActorMemoryLunar.create(agentParameters.withInitWeightLogStd(0), lunarParameters);
        actorMemoryLunarNonZeroForStd = ActorMemoryLunar.create(agentParameters.withInitWeightLogStd(INIT_WEIGHT_LOG_STD), lunarParameters);
    }

    @Test
    void testZeroWeights() {
        assertNotNull(actorMemoryLunar.getMemoryMean());
        assertNotNull(actorMemoryLunar.getMemoryLogStd());
    }


    @Test
    void testActorMeanAndStd() {
        for (int i = 0; i < 100; i++) {
            var state = StateLunar.randomPosAndSpeed(lunarParameters);
            var meanAndStd = actorMemoryLunar.actorMeanAndStd(state);
            var meanAndLogStd = actorMemoryLunar.actorMeanAndLogStd(state);
            assertNotNull(meanAndStd);
            assertEquals(0.0, meanAndStd.mean());
            assertEquals(0.0, meanAndLogStd.logStd());
            assertTrue(meanAndStd.std() >  meanAndLogStd.logStd());
        }
    }

    @Test
    void testActorMeanAndStd_oneForStd() {
        for (int i = 0; i < 100; i++) {
            var state = StateLunar.randomPosAndSpeed(lunarParameters);
            var meanAndStd = actorMemoryLunarNonZeroForStd.actorMeanAndStd(state);
            var meanAndLogStd = actorMemoryLunarNonZeroForStd.actorMeanAndLogStd(state);
            assertEquals(0.0, meanAndStd.mean());
            assertTrue(meanAndLogStd.logStd() > 0);
            assertTrue(meanAndLogStd.logStd()  < INIT_WEIGHT_LOG_STD*5); //5 <=> can be many activations at state
            assertTrue(meanAndStd.std() >  meanAndLogStd.logStd());
        }
    }


    @Test
    void testFit() {
        StateLunar state = StateLunar.randomPosAndSpeed(lunarParameters);
        double adv = 1.0;
        var grad = GradientMeanAndLogStd.of(1.0, 2.0);
        var expStd0 = actorMemoryLunar.actorMeanAndStd(state);
        var dataMean = TrainDataOld.emptyFromErrors();
        var dataStd = TrainDataOld.emptyFromErrors();
        var in = RadialBasisAdapter.asInput(state);
        dataMean.addInAndError(in, grad.mean() * adv);
        dataStd.addInAndError(in, grad.std() * adv);
        actorMemoryLunar.fit(dataMean,dataStd);
        var expStdNew = actorMemoryLunar.actorMeanAndStd(state);
        Assertions.assertTrue(expStdNew.mean() > expStd0.mean());
        Assertions.assertTrue(expStdNew.std() > expStd0.std());
    }


    @Test
    void testCreateClipped_MeanAboveUpperBound() {
        GradientMeanAndLogStd meanAndStd = GradientMeanAndLogStd.of(20.0, 5.0);
        double gradMeanMax = 10.0;
        AgentParameters p = agentParameters.withGradMeanMax(gradMeanMax);
        var clipped = meanAndStd.clip(p.gradMeanMax(), p.gradStdMax());
        assertEquals(gradMeanMax, clipped.mean(), 0.01);
    }

    @Test
    void testCreateClipped_StdWithinBounds() {
        double mean = 5.0;
        var meanAndStd = GradientMeanAndLogStd.of(mean, 5.0);
        double gradMeanMax = 10.0;
        var p = agentParameters.withGradMeanMax(gradMeanMax);
        var clipped = meanAndStd.clip(p.gradMeanMax(), p.gradStdMax());
        assertEquals(mean, clipped.mean(), 0.01);
    }

    @Test
    void testCreateClipped_StdBelowLowerBound() {
        var meanAndStd = GradientMeanAndLogStd.of(10.0, -20.0);
        double gradStdMax = 10.0;
        var p = agentParameters.withGradStdMax(gradStdMax);
        var clipped = meanAndStd.clip(p.gradMeanMax(), p.gradStdMax());
        assertEquals(-gradStdMax, clipped.std(), 0.01);
    }

}
