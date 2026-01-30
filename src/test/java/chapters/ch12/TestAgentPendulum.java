package chapters.ch12;

import chapters.ch12.domain.inv_pendulum.agent.core.AgentPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.ActionPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.factory.AgentParametersFactory;
import chapters.ch12.factory.MockedTrainDataFactory;
import chapters.ch12.factory.TrainerParametersFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static chapters.ch12.domain.inv_pendulum.agent.memory.ActionAndItsValue.findActionValue;


public class TestAgentPendulum {
    public static final int N_ITER = 50;
    public static final double TOL_LOW = 2d, TOL_HIGH = 5d;
    public static final int ZERO_VAL = 0; // -50;
    public static final int LOW_VAL = -10;

    AgentPendulum agent;

    @BeforeEach
    void init() {
        var agentParameters = AgentParametersFactory.createForTest();
        var trainerParameters = TrainerParametersFactory.createForTest();
        agent = AgentPendulum.of(agentParameters, trainerParameters);
        List<List<Double>> listOfLists = MockedTrainDataFactory.getListOfInputs();
        var avList = MockedTrainDataFactory.getActionValueListNegValues(listOfLists);
        List<StatePendulum> stateList = StatePendulum.getStatePendulumList(listOfLists);
        for (int i = 0; i < N_ITER; i++) {
            agent.fit(stateList, avList,trainerParameters.learningRateStartEnd().getFirst());
        }
    }

    @Test
    void whenZeroAngleAndZeroSpeed_thenCorrect() {
        var avList = agent.read(StatePendulum.ofStart(0.0, 0.0));
        var avCCW = findActionValue(ActionPendulum.CCW, avList);
        var avN = findActionValue(ActionPendulum.N, avList);
        var avCW = findActionValue(ActionPendulum.CW, avList);
        Assertions.assertEquals(ZERO_VAL, avCCW.actionValue(), TOL_HIGH);
        Assertions.assertEquals(ZERO_VAL, avN.actionValue(), TOL_LOW);
        Assertions.assertEquals(ZERO_VAL, avCW.actionValue(), TOL_HIGH);
    }

    @Test
    void whenNegAngleAndZeroSpeed_thenCorrect() {
        var avList = agent.read(StatePendulum.ofStart(-1.0, 0.0));
        var avCCW = findActionValue(ActionPendulum.CCW, avList);
        var avN = findActionValue(ActionPendulum.N, avList);
        var avCW = findActionValue(ActionPendulum.CW, avList);
        Assertions.assertEquals(LOW_VAL, avCCW.actionValue(), TOL_LOW);
        Assertions.assertEquals(ZERO_VAL, avN.actionValue(), TOL_HIGH);
        Assertions.assertEquals(ZERO_VAL, avCW.actionValue(), TOL_HIGH);
    }

    @Test
    void whenPosAngleAndZeroSpeed_thenCorrect() {
        var avList = agent.read(StatePendulum.ofStart(1.0, 0.0));
        var avCCW = findActionValue(ActionPendulum.CCW, avList);
        var avN = findActionValue(ActionPendulum.N, avList);
        var avCW = findActionValue(ActionPendulum.CW, avList);
        Assertions.assertEquals(ZERO_VAL, avCCW.actionValue(), TOL_HIGH);
        Assertions.assertEquals(ZERO_VAL, avN.actionValue(), TOL_HIGH);
        Assertions.assertEquals(LOW_VAL, avCW.actionValue(), TOL_LOW);
    }

    @Test
    void chooseActionNoExploration() {
        var bestAction = agent.chooseActionNoExploration(StatePendulum.ofStart(0, 0.0));
        Assertions.assertEquals(ActionPendulum.N, bestAction);
    }

    @Test
    void chooseAction() {
        double probRandom = 1.0;
        var bestAction = agent.chooseAction(StatePendulum.ofStart(0, 0.0), probRandom);
        Assertions.assertTrue(ActionPendulum.getAllActions().contains(bestAction));
    }

    @Test
    void copyParamsFromTargetNet() {
        var avList0 = agent.read(StatePendulum.ofStart(1.0, 0.0));
        agent.copyParamsFromTargetNet();
        var avList1 = agent.read(StatePendulum.ofStart(1.0, 0.0));
        Assertions.assertNotEquals(avList0, avList1);
    }

    @Test
    void copyParamsToTargetNet() {
        agent.copyParamsToTargetNet();
        var avList0 = agent.read(StatePendulum.ofStart(1.0, 0.0));
        agent.copyParamsFromTargetNet();
        var avList1 = agent.read(StatePendulum.ofStart(1.0, 0.0));
        Assertions.assertEquals(avList0, avList1);
    }





}
