package chapters.ch14;

import chapters.ch14.implem.pong.*;
import chapters.ch14.factory.FactoryPongSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestEnvironmentPong2 {

    static final double EPS = 1e-9;
    static PongSettings settings;
    static EnvironmentPong env;

    @BeforeAll
    static void init() {
        settings = FactoryPongSettings.create();
        env = EnvironmentPong.of(settings);
    }

    // --- Reflections ---------------------------------------------------------

    @Test
    void hitHorizontal_reflectsAngleSign() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax() + settings.radiusBall()),
                settings.xMid(),
                +Math.PI/2,   // heading north -> should flip sign on horizontal
                0.1);
        var sr = env.step(state, ActionPong.still);
        assertEquals(-Math.PI/2, wrap(sr.stateNew().headingAngle()), EPS);
        assertFalse(sr.isTerminal());
    }

    @Test
    void hitVertical_reflectsPiMinusPsi() {
        var state = StatePong.of(
                PosXy.of(settings.xMax() + settings.radiusBall(), settings.yMid()),
                settings.xMid(),
                0.0,           // east -> should become west
                0.1);
        var sr = env.step(state, ActionPong.still);
        assertEquals(Math.PI, wrap(sr.stateNew().headingAngle()), EPS);
        assertFalse(sr.isTerminal());
    }

    @Test
    void cornerHit_behaviorIsDefined() {
        var state = StatePong.of(
                PosXy.of(settings.xMax() + settings.radiusBall(),
                        settings.yMax() + settings.radiusBall()),
                settings.xMid(),
                Math.PI / 4, // NE
                0.1);
        double newPsi = env.step(state, ActionPong.still).stateNew().headingAngle();

        assertEquals(wrap(Math.PI / 4 + Math.PI), wrap(newPsi), 1e-9); // NE -> SW
    }

    @Test
    void speedMagnitudeIsPreservedOnReflection() {
        var state = StatePong.of(
                PosXy.of(settings.xMax() + settings.radiusBall(), settings.yMid()),
                settings.xMid(),
                0.3,
                0.1);
        var sr = env.step(state, ActionPong.still);
        var v = sr.stateNew().velBall();
        assertEquals(settings.speedBall(), v.abs(), EPS);
    }

    // --- Paddle clipping & movement -----------------------------------------

    @Test
    void paddleClipsAtLeftEdge() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax()),
                settings.xMin(),         // too far left
                0.0,
                0.1);
        var sr = env.step(state, ActionPong.right);
        assertEquals(settings.xMinPlusHalfPaddle(),
                sr.stateNew().posPaddle().x(), EPS);
    }

    @Test
    void noOpDoesNotMovePaddle() {
        var x0 = settings.xMid();
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax()),
                x0,
                0.0,
                0.1);
        var sr = env.step(state, ActionPong.still);
        assertEquals(x0, sr.stateNew().posPaddle().x(), EPS);
    }

    // --- Rewards, tStill, and failure ---------------------------------------

    @Test
    void timeStill_ResetsOnMove_NoHit() {
        double t0 = 0.7;
        // Ball high and heading up -> no hit this step
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax()),
                settings.xMid(),
                +Math.PI / 2,   // north
                t0
        );

        var sr = env.step(state, ActionPong.left);
        assertEquals(0.0, sr.stateNew().timeStill(), EPS);
        assertFalse(sr.isTerminal());
        assertFalse(sr.isFail());
    }

    @Test
    void timeStill_AccumulatesOnIdle_NoHit() {
        double t0 = 0.3;
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax()),
                settings.xMid(),
                +Math.PI / 2,   // north, no hit
                t0
        );

        var sr = env.step(state, ActionPong.still);
        assertEquals(t0 + settings.timeStep(), sr.stateNew().timeStill(), EPS);
        assertFalse(sr.isTerminal());
        assertFalse(sr.isFail());
    }

    @Test
    void timeStill_ResetsOnPaddleHit_NoMove() {
        double t0 = 0.5;
        // Geometry chosen to hit the centered paddle this step
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.radiusBall()),
                settings.xMid(),          // paddle centered
                -Math.PI / 2,             // south
                t0
        );

        var sr = env.step(state, ActionPong.still);

        System.out.println("sr = " + sr);

        assertEquals(0.0, sr.stateNew().timeStill(), EPS);
        assertFalse(sr.isTerminal());
        assertFalse(sr.isFail());
        assertTrue(sr.reward() > 0); // got the wait bonus
    }

    @Test
    void timeStill_ResetsOnPaddleHit_WithMove() {
        double t0 = 0.4;
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.radiusBall()),
                settings.xMid(),
                -Math.PI / 2,  // south -> hit
                t0
        );

        var sr = env.step(state, ActionPong.left);
        assertEquals(0.0, sr.stateNew().timeStill(), EPS);
        assertFalse(sr.isTerminal());
        assertFalse(sr.isFail());
        // reward = +t0 (hit bonus) -1 (move penalty)
        assertEquals(t0 - 1.0, sr.reward(), 1e-6);
    }

    // --- reward & penalties ---------------------------------------------------

    @Test
    void reward_UsesPreStepTimeStill_OnHit() {
        double tShort = 0.2, tLong = 0.6;

        var sShort = StatePong.of(
                PosXy.of(settings.xMid(), settings.radiusBall()),
                settings.xMid(), -Math.PI / 2, tShort);
        var sLong = StatePong.of(
                PosXy.of(settings.xMid(), settings.radiusBall()),
                settings.xMid(), -Math.PI / 2, tLong);

        var rShort = env.step(sShort, ActionPong.still).reward(); // = +tShort
        var rLong  = env.step(sLong,  ActionPong.still).reward(); // = +tLong

        assertEquals(tLong - tShort, rLong - rShort, 1e-9);
    }

    @Test
    void reward_MovePenaltyOnlyWhenMoving_NoHit() {
        // No hit scenario to isolate move penalty
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax()),
                settings.xMid(),
                +Math.PI / 2, // north
                0.5
        );
        double rIdle = env.step(state, ActionPong.still).reward();
        double rLeft = env.step(state, ActionPong.left).reward();
        double rRight = env.step(state, ActionPong.right).reward();

        assertEquals(rIdle - 1.0, rLeft, EPS);
        assertEquals(rIdle - 1.0, rRight, EPS);
    }

    // --- fail logic sanity check ---------------------------------------------

    @Test
    void missPaddle_IsTerminalAndFail() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), -settings.radiusBall()),
                settings.xMaxMinusHalfPaddle(), // far right -> miss
                -Math.PI / 2,                   // south
                0.1
        );
        var sr = env.step(state, ActionPong.still);
        assertTrue(sr.isTerminal());
        assertTrue(sr.isFail());
        assertTrue(sr.reward() <= -100.0);
    }


    @Test
    void rewardUsesPreHitTstill() {
        var tShort = 0.2;
        var tLong  = 0.6;
        var sShort = StatePong.of(
                PosXy.of(settings.xMid(), settings.radiusBall()),
                settings.xMid(),
                -Math.PI/2,
                tShort);
        var sLong = StatePong.of(
                PosXy.of(settings.xMid(), settings.radiusBall()),
                settings.xMid(),
                -Math.PI/2,
                tLong);

        var rShort = env.step(sShort, ActionPong.still).reward();
        var rLong  = env.step(sLong, ActionPong.still).reward();

        // movement penalty and fail are identical; difference must be tLong - tShort
        assertEquals(tLong - tShort, rLong - rShort, EPS);
    }

    @Test
    void movePenaltyOnlyWhenMoving() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax()),
                settings.xMid(),
                0.0,
                0.5);

        var rNoMove = env.step(state, ActionPong.still).reward();
        var rMoveL  = env.step(state, ActionPong.left).reward();
        var rMoveR  = env.step(state, ActionPong.right).reward();

        assertEquals(rNoMove - 1.0, rMoveL, EPS);
        assertEquals(rNoMove - 1.0, rMoveR, EPS);
    }

    @Test
    void missPaddleFailsWithBigNegativeReward() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), -settings.radiusBall()),
                settings.xMaxMinusHalfPaddle(), // paddle far right -> miss
                -Math.PI/2,   // heading south
                0.1);
        var sr = env.step(state, ActionPong.still);
        assertTrue(sr.isFail());
        assertTrue(sr.isTerminal());
        assertTrue(sr.reward() <= -100); // includes -100 fail plus maybe move
    }

    @Test
    void wallBounceIsNotTerminal() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax() + settings.radiusBall()),
                settings.xMid(),
                +Math.PI/2,
                0.1);
        var sr = env.step(state, ActionPong.still);
        assertFalse(sr.isTerminal());
        assertFalse(sr.isFail());
    }

    // --- Immutability --------------------------------------------------------

    @Test
    void stepDoesNotMutateInputState() {
        var state = StatePong.of(
                PosXy.of(settings.xMid(), settings.yMax()),
                settings.xMid(),
                0.1,
                0.2);
        // shallow copy for comparison (adjust if you have equals)
        var ball0 = state.posBall();
        var paddleX0 = state.posPaddle().x();
        var psi0 = state.headingAngle();
        var t0 = state.timeStill();

        env.step(state, ActionPong.still);

        assertEquals(ball0, state.posBall());
        assertEquals(paddleX0, state.posPaddle().x(), EPS);
        assertEquals(psi0, state.headingAngle(), EPS);
        assertEquals(t0, state.timeStill(), EPS);
    }

    // --- helper: wrap angle to (-pi, pi]
    private static double wrap(double a) {
        double x = ((a + Math.PI) % (2*Math.PI) + 2*Math.PI) % (2*Math.PI) - Math.PI;
        return x <= -Math.PI ? x + 2*Math.PI : x;
    }
}


