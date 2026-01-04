package chapters.ch14;

import chapters.ch14.environments.pong.*;
import chapters.ch14.factory.FactoryPongSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestStatePong {
    public static final double TOL = 1e-6;
    PongSettings settings;

    @BeforeEach
    void init() {
        settings = FactoryPongSettings.create();
    }

    @Test
    void testOf() {
        var posBall = PosXy.of(settings.xMinPlusHalfRadiusBall(), settings.yMaxMinusHalfRadiusBall());
        VelocityXy velocityBall = VelocityXy.of(3.0, 4.0);
        double posPaddleX = settings.xMid();
        double headingAngle = 6.0;
        double tStill = 7.0;
        StatePong statePong = StatePong.of(posBall, velocityBall, posPaddleX, headingAngle, tStill);
        boolean isOkState=statePong.isOkPosBallAndPaddle(settings);
        assertEquals(posBall, statePong.posBall());
        assertEquals(velocityBall, statePong.velBall());
        assertEquals(PosXy.of(posPaddleX, StatePong.PADDLE_Y), statePong.posPaddle());
        assertEquals(headingAngle, statePong.headingAngle(), TOL);
        assertEquals(tStill, statePong.timeStill(), TOL);
        assertTrue(isOkState);
    }

    @Test
    void testOfFailValidate() {
        PosXy posBall = PosXy.of(1.0, settings.yMax()*2);
        VelocityXy velocityBall = VelocityXy.of(3.0, 4.0);
        double posPaddleX = 5.0;
        double headingAngle = 6.0;
        double tStill = 7.0;
        StatePong statePong = StatePong.of(posBall, velocityBall, posPaddleX, headingAngle, tStill);
        boolean isOkState=statePong.isOkPosBallAndPaddle(settings);

        assertFalse(isOkState);
    }


    @Test
    void testNewPosBall() {
        PosXy posBall = PosXy.of(1.0, 2.0);
        VelocityXy velocityBall = VelocityXy.of(3.0, 4.0);
        double posPaddleX = 0.0;
        StatePong statePong = StatePong.of(posBall, velocityBall, posPaddleX, 0.0, 0.0);
        double dt = settings.timeStep();
        PosXy newPosBall = statePong.newPosBall(velocityBall, dt);
        assertEquals(posBall.x() + velocityBall.x() * dt, newPosBall.x(), TOL);
        assertEquals(posBall.y() + velocityBall.y() * dt, newPosBall.y(), TOL);
    }

    @Test
    void testNewXPosPaddle() {
        double posPaddleX = 1.0;
        ActionPong action = ActionPong.right;
        double dt = settings.timeStep();
        StatePong statePong = StatePong.of(PosXy.of(0.0, 0.0), VelocityXy.of(0.0, 0.0), posPaddleX, 0.0, 0.0);
        PosXy newXPosPaddle = statePong.newXPosPaddle(action, dt);
        assertEquals(posPaddleX + action.velPaddleX() * dt, newXPosPaddle.x(), TOL);
    }

    @Test
    void testCopy() {
        PosXy posBall = PosXy.of(1.0, 2.0);
        VelocityXy velocityBall = VelocityXy.of(3.0, 4.0);
        double headingAngle = 7.0;
        double tStill = 8.0;
        StatePong statePong = StatePong.of(posBall, velocityBall, 5.0, headingAngle, tStill);
        StatePong copiedStatePong = statePong.copy();
        assertEquals(statePong.posBall(), copiedStatePong.posBall());
        assertEquals(statePong.velBall(), copiedStatePong.velBall());
        assertEquals(statePong.posPaddle(), copiedStatePong.posPaddle());
        assertEquals(statePong.headingAngle(), copiedStatePong.headingAngle(), TOL);
        assertEquals(statePong.timeStill(), copiedStatePong.timeStill(), TOL);
        assertEquals(statePong.finiteState(), copiedStatePong.finiteState());
    }

    @Test
    void testCopyOriginalNotAffected() {
        PosXy posBall = PosXy.of(1.0, 2.0);
        VelocityXy velocityBall = VelocityXy.of(3.0, 4.0);
        double headingAngle = 7.0;
        double tStill = 8.0;
        double posPaddleX = 5.0;
        StatePong statePong = StatePong.of(posBall, velocityBall, posPaddleX, headingAngle, tStill);
        StatePong copiedStatePong = statePong.copy();
        double dt = settings.timeStep();

        copiedStatePong.newPosBall(VelocityXy.of(1.0, 1.0), 0.0);
        copiedStatePong.newXPosPaddle(ActionPong.right, dt);

        assertEquals(posBall, statePong.posBall());
        assertEquals(posPaddleX, statePong.posPaddle().x());
    }


    @Test
    void testEquals() {
        PosXy posBall = PosXy.of(1.0, 2.0);
        VelocityXy velocityBall = VelocityXy.of(3.0, 4.0);
        double headingAngle = 7.0;
        double tStill = 8.0;
        double posPaddleX = 5.0;
        StatePong statePong1 = StatePong.of(posBall, velocityBall, posPaddleX, headingAngle, tStill);
        StatePong statePong2 = StatePong.of(posBall, velocityBall, posPaddleX, headingAngle, tStill);
        assertTrue(statePong1.equals(statePong2));
    }

    @Test
    void testHashCode() {
        PosXy posBall = PosXy.of(1.0, 2.0);
        VelocityXy velocityBall = VelocityXy.of(3.0, 4.0);
        double paddleX = 5.0;
        double headingAngle = 7.0;
        double tStill = 8.0;
        StatePong statePong1 = StatePong.of(posBall, velocityBall, paddleX, headingAngle, tStill);
        StatePong statePong2 = StatePong.of(posBall, velocityBall, paddleX, headingAngle, tStill);

        assertEquals(statePong1.hashCode(), statePong2.hashCode());
    }

}
