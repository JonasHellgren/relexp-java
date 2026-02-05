package chapters.ch14;

import chapters.ch14.domain.planner.Planner;
import chapters.ch14.implem.pong.*;
import chapters.ch14.implem.pong_memory.BallHitFloorCalculator;
import chapters.ch14.implem.pong_memory.LongMemoryZero;
import chapters.ch14.implem.pong_memory.StateAdapterPong;
import chapters.ch14.factory.FactoryPlanner;
import chapters.ch14.factory.FactoryPongSettings;
import chapters.ch14.factory.FactoryStatePong;
import chapters.ch14.factory.FactoryTrainerSettings;
import core.foundation.util.unit_converter.UnitConverterUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.Supplier;

public class TestPlannerWithAlwaysZeroMemory {

    static final double HEAD_SOUTH = -Math.PI / 2;
    static final double STILL_FOR_A_WHILE = 0.5;

    PongSettings envSettings;
    Planner<StateLongPong, StatePong, ActionPong> planner;
    LongMemoryZero memory;

    @BeforeEach
    void init() {
        envSettings = FactoryPongSettings.create();
        var environment = EnvironmentPong.of(envSettings);
        var calculator = BallHitFloorCalculator.of(environment, envSettings);
        var trainerSettings = FactoryTrainerSettings.forTest();
        planner = FactoryPlanner.forTest(calculator,trainerSettings);
        memory = LongMemoryZero.create();
    }

    @Test
    void givenBallMid_whenPaddleMid_whenNoChange() {
        Supplier<StatePong> stateXMid = () -> StatePong.of(
                PosXy.of(envSettings.xMid(), envSettings.yMid()),
                envSettings.xMid(),  //paddle mid
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);
        var result = planner.plan(stateXMid, memory);
        Assertions.assertTrue(result.nRollouts() > 0);
        Assertions.assertTrue(result.nRolloutsSucceeded() > 0);
        Assertions.assertTrue(result.returnBest() > -100);
        Assertions.assertTrue(result.actions().contains(ActionPong.still));
    }


    @Test
    void givenBallMid_whenPaddleRight_whenChange() {
        Supplier<StatePong> stateXMax = () -> StatePong.of(
                PosXy.of(envSettings.xMid(), envSettings.yMaxMinusHalfRadiusBall()),
                envSettings.xMaxMinusHalfPaddle(),  //paddle far right
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);

        var result = planner.plan(stateXMax, memory);
        Assertions.assertTrue(result.isSomeRolloutsSuccess());
        Assertions.assertFalse(result.isSecondRolloutTry());
        Assertions.assertTrue(result.returnBest() > -100);
        Assertions.assertTrue(result.actions().contains(ActionPong.left));
    }

    @Test
    void givenBallRight_whenPaddleRight_whenNoChange() {
        Supplier<StatePong> stateXMax = () -> StatePong.of(
                PosXy.of(envSettings.yMaxMinusHalfRadiusBall(), envSettings.yMaxMinusHalfRadiusBall()),
                envSettings.xMaxMinusHalfPaddle(),  //paddle far right
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);

        var result = planner.plan(stateXMax, memory);
        Assertions.assertTrue(result.isSomeRolloutsSuccess());
        Assertions.assertFalse(result.isSecondRolloutTry());
        Assertions.assertTrue(result.returnBest() > -100);
        Assertions.assertTrue(result.actions().contains(ActionPong.still));
    }

    @Test
    void givenBallRight_whenPaddleMid_whenChange() {
        Supplier<StatePong> stateXMax = () -> StatePong.of(
                PosXy.of(envSettings.xMaxMinusHalfRadiusBall(), envSettings.yMaxMinusHalfRadiusBall()),
                envSettings.xMid(),  //paddle mid
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);

        var result = planner.plan(stateXMax, memory);
        Assertions.assertTrue(result.isSomeRolloutsSuccess());
        Assertions.assertFalse(result.isSecondRolloutTry());
        Assertions.assertTrue(result.returnBest() > -100);
        Assertions.assertTrue(result.actions().contains(ActionPong.right));
    }

    @Test
    void givenBallRightYMid_whenPaddleLeft_whenFail() {
        Supplier<StatePong> stateXMax = () -> StatePong.of(
                PosXy.of(envSettings.xMaxMinusHalfRadiusBall(), envSettings.yMid()),
                envSettings.xMinPlusHalfRadiusBall(),  //paddle left
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);

        var result = planner.plan(stateXMax, memory);
        Assertions.assertTrue(result.isAllRolloutsFailed());
        Assertions.assertTrue(result.isSecondRolloutTry());
    }

    @Test
    void givenDoomedState_whenFail() {
        var factory = FactoryStatePong.of(envSettings);
        var stateDoomed = factory.doomed();
        Supplier<StatePong> sup = () -> stateDoomed;
        var result = planner.plan(sup, memory);
        Assertions.assertTrue(result.isAllRolloutsFailed());
        Assertions.assertTrue(result.isSecondRolloutTry());
    }

    @Test
    void givenBallLeftYMid_whenPaddleRight_whenFail() {
        Supplier<StatePong> stateXMax = () -> StatePong.of(
                PosXy.of(envSettings.xMinPlusHalfRadiusBall(), envSettings.yMid()),
                envSettings.xMaxMinusHalfRadiusBall(),  //paddle right
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);

        var result = planner.plan(stateXMax, memory);
        Assertions.assertTrue(result.isAllRolloutsFailed());
        Assertions.assertTrue(result.isSecondRolloutTry());
    }

    @Test
    void givenCrazy_whenFail() {
        Supplier<StatePong> stateXMax = () -> StatePong.of(
                PosXy.of(0.38,0.8),
                0.24,  //paddle right
                UnitConverterUtil.convertDegreesToRadians(-116.06),
                STILL_FOR_A_WHILE);

        var result = planner.plan(stateXMax, memory);
        Assertions.assertFalse(result.isAllRolloutsFailed());
        Assertions.assertFalse(result.isSecondRolloutTry());
    }

    @Test
    void givenCrazy2_whenFail() {
        Supplier<StatePong> ss = () -> StatePong.of(
                PosXy.of(0.47,0.53),
                0.92,  //paddle right
                UnitConverterUtil.convertDegreesToRadians(-122.25),
                STILL_FOR_A_WHILE);

        var result = planner.plan(ss, memory);

        System.out.println("result = " + result);
        var environment= EnvironmentPong.of(envSettings);
        var timeToHitCalculator= BallHitFloorCalculator.of(environment, envSettings);
        var stateLong = StateAdapterPong.stateLong(timeToHitCalculator, ss.get());
        System.out.println("stateLong = " + stateLong);

        Assertions.assertTrue(stateLong.timeHit()<stateLong.deltaX());
        Assertions.assertTrue(result.isAllRolloutsFailed());
        Assertions.assertTrue(result.isSecondRolloutTry());
    }


    @Test
    void givenBallRightYMax_whenPaddleLeft_whenNotFail() {
        Supplier<StatePong> stateXMax = () -> StatePong.of(
                PosXy.of(envSettings.xMaxMinusHalfRadiusBall(), envSettings.yMaxMinusHalfRadiusBall()),
                envSettings.xMinPlusHalfRadiusBall(),  //paddle left
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);

        var result = planner.plan(stateXMax, memory);
        Assertions.assertTrue(result.isSomeRolloutsSuccess());
    }

    @Test
    void givenBallLeftYMax_whenPaddleRight_whenNotFail() {
        Supplier<StatePong> stateXMax = () -> StatePong.of(
                PosXy.of(envSettings.xMinPlusHalfRadiusBall(), envSettings.yMaxMinusHalfRadiusBall()),
                envSettings.xMaxMinusHalfRadiusBall(),  //paddle right
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);

        var result = planner.plan(stateXMax, memory);
        Assertions.assertTrue(result.isSomeRolloutsSuccess());
    }

}
