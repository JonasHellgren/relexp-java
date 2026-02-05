package chapters.ch14;

import chapters.ch14.implem.pong.FiniteState;
import chapters.ch14.implem.pong.PongSettings;
import chapters.ch14.implem.pong.PosXy;
import chapters.ch14.factory.FactoryPongSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestFiniteState {

    double xMid;
    PongSettings settings;
    PosXy posPaddle;

    @BeforeEach
    void init() {
        settings = FactoryPongSettings.create();
        xMid=settings.xMid();
        posPaddle = PosXy.of(xMid, 0);
    }

    @Test
    void testOf_StatePongAndPongSettings_ReturnsFiniteState() {
        var posBall = PosXy.of(settings.xMax()-settings.radiusBall()*2, settings.yMax() / 2);
        var finiteState = FiniteState.of(posBall, posPaddle, settings);
        System.out.println("finiteState = " + finiteState);
        assertNotNull(finiteState);
        assertFalse(finiteState.isBallParallelPaddle());
        assertFalse(finiteState.isHitVert());
        assertFalse(finiteState.isHitHor());
        assertFalse(finiteState.isBallHitPaddle());
    }

    @Test
    void testOf_IsBallParallelPaddle_ReturnsTrue() {
        var posBall = PosXy.of(xMid, settings.yMax() / 2);
        var finiteState = FiniteState.of(posBall, posPaddle, settings);
        assertTrue(finiteState.isBallParallelPaddle());
    }

    @Test
    void testOf_IsHitVert_ReturnsTrue() {
        var posBall = PosXy.of(settings.xMax() + settings.radiusBall(), 0);
        var finiteState = FiniteState.of(posBall, posPaddle, settings);
        assertTrue(finiteState.isHitVert());
    }

    @Test
    void testOf_IsHitHor_ReturnsTrue() {
        var posBall = PosXy.of(xMid, -settings.radiusBall());
        var finiteState = FiniteState.of(posBall, posPaddle, settings);
        System.out.println("finiteState = " + finiteState);
        assertTrue(finiteState.isHitHor());
    }

    @Test
    void testOf_IsBallHitPaddle_ReturnsTrue() {
        var posBall = PosXy.of(xMid, -settings.radiusBall());
        var finiteState = FiniteState.of(posBall, posPaddle, settings);
        assertTrue(finiteState.isBallHitPaddle());
    }

}
