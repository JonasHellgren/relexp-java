package chapters.ch14;

import chapters.ch14.domain.planner.Planner;
import chapters.ch14.environments.pong.*;
import chapters.ch14.environments.pong_memory.BallHitFloorCalculator;
import chapters.ch14.environments.pong_memory.LongMemoryZero;
import chapters.ch14.factory.FactoryPlanner;
import chapters.ch14.factory.FactoryPlanningSettings;
import chapters.ch14.factory.FactoryPongSettings;
import chapters.ch14.factory.FactoryTrainerSettings;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.Supplier;

public class TestPlannerZeroShallowPlanner {

    static final double HEAD_SOUTH = -Math.PI / 2;
    static final double STILL_FOR_A_WHILE = 0.5;
    public static final int MAX_DEPTH_SHALLOW_PLANNER = 5;

    PongSettings envSettings;
    Planner<StateLongPong, StatePong, ActionPong> planner, plannerShallow;
    LongMemoryZero memory;

    @BeforeEach
    void init() {
        envSettings = FactoryPongSettings.create();
        var environment = EnvironmentPong.of(envSettings);
        var calculator = BallHitFloorCalculator.of(environment, envSettings);
        var trainerSettings = FactoryTrainerSettings.forRunning();
        planner = FactoryPlanner.forTest(calculator,trainerSettings);
        plannerShallow = FactoryPlanner.of(
                FactoryPlanningSettings.forTest().withMaxDepth(MAX_DEPTH_SHALLOW_PLANNER),
                calculator,trainerSettings);
        memory = LongMemoryZero.create();
    }

    @Test
    void givenBallLeft_whenPaddleRight_whenNoLeftActionNotShallow() {
        var result = planner.plan(getStatePongSupplier(), memory);
        Assertions.assertTrue(result.isSomeRolloutsSuccess());
        Assertions.assertTrue(result.actions().contains(ActionPong.left));
    }


    @Test
    void givenBallLeft_whenPaddleRight_whenNoLeftActionShallow() {
        var resultShallow = plannerShallow.plan(getStatePongSupplier(), memory);
        Assertions.assertFalse(resultShallow.actions().contains(ActionPong.left));
    }

    @NotNull
    private Supplier<StatePong> getStatePongSupplier() {
        return () -> StatePong.of(
                PosXy.of(envSettings.xMinPlusHalfRadiusBall(), envSettings.yMaxMinusHalfRadiusBall()),
                envSettings.xMaxMinusHalfPaddle(),  //paddle far right
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);
    }


}
