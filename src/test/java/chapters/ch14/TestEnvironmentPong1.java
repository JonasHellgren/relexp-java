package chapters.ch14;

import chapters.ch14.implem.pong.*;
import chapters.ch14.factory.FactoryPongSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestEnvironmentPong1 {

    static final double HEAD_SOUTH = -Math.PI / 2;
    static final double HEAD_NORTH = Math.PI / 2;
    static final double STILL_FOR_A_WHILE = 0.5;
    private static PongSettings settings;
    private static EnvironmentPong environment;

    @BeforeAll
    static void init() {
        settings = FactoryPongSettings.create();
        environment = EnvironmentPong.of(settings);
    }

    @Test
    void testStep_BallHitPaddle_RewardIsPositive() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.radiusBall()),
                settings.xMid(),  //paddle in the middle
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);
        var action = ActionPong.still;
        var sr = environment.step(state, action);
        assertFalse(sr.isTerminal());
        assertFalse(sr.isFail());
        assertTrue(sr.reward() > 0);  //been still for a while
    }

    @Test
    void testStep_BallMissedPaddle_RewardIsNegative() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), -settings.radiusBall()),
                settings.xMaxMinusHalfPaddle(),  //paddle far right
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);
        var action = ActionPong.still;
        var result = environment.step(state, action);
        assertTrue(result.isTerminal());
        assertTrue(result.isFail());
        assertTrue(result.reward() < 0);
    }

    @Test
    void testStep_PaddleMoved() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax()),
                settings.xMaxMinusHalfPaddle(),  //paddle far right
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);
        var action = ActionPong.left;
        var result = environment.step(state, action);

        assertFalse(result.isTerminal());
        assertFalse(result.isFail());
        assertTrue(result.stateNew().posPaddle().x() < settings.xMaxMinusHalfPaddle());
    }

    @Test
    void testClipXForPaddle_PaddleAtEdge_PaddleClipped() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax()),
                settings.xMax(),  //paddle to far right
                HEAD_SOUTH,
                STILL_FOR_A_WHILE);
        var action = ActionPong.left;
        PosXy clippedPos = environment.step(state, action).stateNew().posPaddle();

        assertEquals(settings.xMaxMinusHalfPaddle(), clippedPos.x());
    }

    @Test
    void testNewPsi_HitVertical_PsiIsReflected() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax() + settings.radiusBall()),
                settings.xMax(),  //paddle to far right
                HEAD_NORTH,
                STILL_FOR_A_WHILE);
        double newPsi = environment.step(state, ActionPong.still).stateNew().headingAngle();

        assertEquals(HEAD_SOUTH, newPsi);
    }

}
