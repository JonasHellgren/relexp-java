package chapters.ch11;

import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.factory.LunarAgentParamsFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import chapters.ch11.helper.RadialBasisAdapter;
import core.foundation.gadget.training.TrainDataOld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestAgent {

    public static final int ADV = 1;
    AgentLunar agent;
    LunarParameters lunarParameters;
    StateLunar stateRandomPosAndSpeed, state0and0;

    @BeforeEach
    void init() {
        lunarParameters = LunarEnvParamsFactory.produceDefault();
        var ap = LunarAgentParamsFactory.newDefault(lunarParameters);
        agent = AgentLunar.zeroWeights(ap, lunarParameters);
        stateRandomPosAndSpeed = StateLunar.randomPosAndSpeed(lunarParameters);
        state0and0 = StateLunar.of(0, 0);
    }


    @Test
    void testReadCritic() {
        double value = agent.readCritic(stateRandomPosAndSpeed);
        assertEquals(0.0, value, 0.0);
    }

    @Test
    void givenSameActionAsActorMean_thenCorrectGradientMeanAndLogStd() {
        double action = agent.readActor(stateRandomPosAndSpeed).mean();
        var grad = agent.gradientMeanAndLogStd(stateRandomPosAndSpeed, action);
        Assertions.assertEquals(0.0, grad.mean(), 0.0);
        Assertions.assertEquals(-ADV, grad.std(), 0.0);
    }

    @Test
    void givenLargerActionAsActorMean_thenCorrectGradientMeanAndLogStd() {
        double action = agent.readActor(stateRandomPosAndSpeed).mean() + 5;
        var grad = agent.gradientMeanAndLogStd(stateRandomPosAndSpeed, action);
        Assertions.assertTrue(grad.mean() > 0.0);
        Assertions.assertTrue(grad.std() > 0.0);
    }

    @Test
    void givenSmallerActionAsActorMean_thenCorrectGradientMeanAndLogStd() {
        double action = agent.readActor(stateRandomPosAndSpeed).mean() - 5;
        var grad = agent.gradientMeanAndLogStd(stateRandomPosAndSpeed, action);
        Assertions.assertTrue(grad.mean() < 0.0);
        Assertions.assertTrue(grad.std() > 0.0);
    }

    @Test
    void whenChooseAction_thenActionCloseToZero() {
        double action = agent.chooseAction(stateRandomPosAndSpeed);
        double std = agent.readActor(stateRandomPosAndSpeed).std();
        assertEquals(0.0, action, std * 3);
    }


    @Test
    void testFitActorWithNegativeAction() {
        double action0 = agent.readActor(state0and0).mean();
        int actionApplied = -1;
        var dataMean = TrainDataOld.emptyFromErrors();
        var in = RadialBasisAdapter.asInput(state0and0);
        dataMean.addInAndError(in, actionApplied*ADV);
        int valTarget = 10;
        fitCritic(valTarget);
        var dummyData=dataMean;
        agent.fitActorUseCriticActivations(dataMean,dummyData);
        double action = agent.readActor(state0and0).mean();
        Assertions.assertTrue(action < action0);
    }


    @Test
    void testFitActorWithPositiveAction() {
        double action0 = agent.readActor(state0and0).mean();
        int actionApplied = 1;
        var dataMean = TrainDataOld.emptyFromErrors();
        var in = RadialBasisAdapter.asInput(state0and0);
        dataMean.addInAndError(in, actionApplied*ADV);
        int valTarget = 10;
        fitCritic(valTarget);
        var dummyData=dataMean;
        agent.fitActorUseCriticActivations(dataMean,dummyData);
        double action = agent.readActor(state0and0).mean();
        Assertions.assertTrue(action > action0);
    }


    @Test
    void testFitCritic() {
        double value0 = agent.readCritic(state0and0);
        int valTarget = 10;
        fitCritic(valTarget);
        double value = agent.readCritic(state0and0);
        Assertions.assertTrue(value > value0);
    }

    @Test
    void fitCriticOneState_thenValueOtherStateNotAffected() {
        double value0 = agent.readCritic(state0and0);
        int valTarget = 10;
        fitCritic(valTarget);
        double value = agent.readCritic(state0and0);
        double valueOther = agent.readCritic(StateLunar.of(10, 0));
        Assertions.assertTrue(value > value0);
        Assertions.assertEquals(value0, valueOther, 1e-4);
    }


    private void fitCritic(int valTarget) {
        var data= TrainDataOld.emptyFromOutputs();
        var in = RadialBasisAdapter.asInput(state0and0);
        data.addIAndOut(in, valTarget);
        agent.fitCritic(data);
    }


}
