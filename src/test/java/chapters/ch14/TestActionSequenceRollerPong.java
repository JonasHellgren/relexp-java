package chapters.ch14;

import chapters.ch14.domain.planner.ActionSequenceRoller;
import chapters.ch14.domain.planner.RollingResult;
import chapters.ch14.domain.environment.EnvironmentI;
import chapters.ch14.domain.interfaces.StateInterpreterI;
import chapters.ch14.implem.pong.*;
import chapters.ch14.implem.pong_memory.BallHitFloorCalculator;
import chapters.ch14.implem.pong_memory.LongMemoryZero;
import chapters.ch14.implem.pong_memory.StateInterpreterPong;
import chapters.ch14.factory.FactoryPongSettings;
import chapters.ch14.factory.FactoryTrainerSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestActionSequenceRollerPong {

    static final double HEAD_SOUTH = -Math.PI / 2;
    static final double HEAD_NORTH = Math.PI / 2;
    static final double STILL_FOR_A_WHILE = 0.5;
    public static final double TOL = 0.001;


    EnvironmentI<StatePong, ActionPong> environment;
    StateInterpreterI<StateLongPong, StatePong> interpreter;
    ActionSequenceRoller<StateLongPong, StatePong, ActionPong> actionSequenceRoller;
    StatePong state;
    LongMemoryZero memory;

    @BeforeEach
    void init() {
        var envSettings = FactoryPongSettings.create();
        environment = EnvironmentPong.of(envSettings);
        var calculator = BallHitFloorCalculator.of(environment, envSettings);
        interpreter = StateInterpreterPong.create(calculator);
        var trainerSettings = FactoryTrainerSettings.forRunning();
        actionSequenceRoller = ActionSequenceRoller.of(environment, interpreter, trainerSettings);
        state = StatePong.of(
                PosXy.of(envSettings.xMid(), envSettings.yMid()),
                envSettings.xMax(),  //paddle far right
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);
        memory = LongMemoryZero.create();
    }

    @Test
    void givenFewStillActions_thenNotTerminalZeroAccSum() {
        List<ActionPong> actions = List.of(ActionPong.still, ActionPong.still);
        RollingResult result = actionSequenceRoller.roll(state, actions, memory);
        Assertions.assertFalse(result.isEndFail());
        Assertions.assertFalse(result.isEndTerminal());
        Assertions.assertEquals(0, result.accRewards(), TOL);
    }


    @Test
    void givenManyStillActions_thenTerminalNotZeroAccSum() {
        List<ActionPong> actions = new ArrayList<>();
        int nActions = 100;
        IntStream.range(0, nActions).forEach(i -> actions.add(ActionPong.still));
        RollingResult result = actionSequenceRoller.roll(state, actions, memory);
        Assertions.assertTrue(result.isEndFail());
        Assertions.assertTrue(result.isEndTerminal());
        Assertions.assertNotEquals(0, result.accRewards(), TOL);
        Assertions.assertTrue(result.nSteps() < nActions);
    }


}
