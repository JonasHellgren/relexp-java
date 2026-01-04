package chapters.ch14;

import chapters.ch14.environments.pong.EnvironmentPong;
import chapters.ch14.environments.pong.PongSettings;
import chapters.ch14.environments.pong.PosXy;
import chapters.ch14.environments.pong.StatePong;
import chapters.ch14.environments.pong_memory.BallHitFloorCalculator;
import chapters.ch14.factory.FactoryPongSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestTimeToHitFloorCalculator {

    static final double HEAD_SOUTH = -Math.PI / 2;
    static final double HEAD_SOUTH_EAST = -Math.PI / 4;
    static final double HEAD_NORTH = Math.PI / 2;
    static final double STILL_FOR_A_WHILE = 0.5;
    public static final double TOL = 0.1;

    private BallHitFloorCalculator calculator;
    private PongSettings settings;

    @BeforeEach
    void setup() {
        settings = FactoryPongSettings.create();
        var environment = EnvironmentPong.of(settings);
        calculator = BallHitFloorCalculator.of(environment,settings);
    }

    @Test
    void givenBallInMidTopAndPaddleRight_whenHeadSouth() {
        var state = stateBallInMidAndPaddleMid_HEAD_SOUTH(settings.yMid(), HEAD_SOUTH);
        double time = calculator.calculate(state).timeHit();
        assertTrue(time > 0);
    }

    @Test
    void whenHeadNorth_moreTimeThanHeadSouth() {
        var state = stateBallInMidAndPaddleMid_HEAD_SOUTH(settings.yMid(), HEAD_NORTH);
        var resN = calculator.calculate(state);
        state = stateBallInMidAndPaddleMid_HEAD_SOUTH(settings.yMid(),HEAD_SOUTH);
        var resS = calculator.calculate(state);
        System.out.println("resN = " + resN);
        assertTrue(resN.timeHit() > resS.timeHit());
        assertEquals(settings.xMid(),resN.xBall(), TOL);
        assertEquals(settings.xMid(),resS.xBall(), TOL);
    }

    @Test
    void whenHeaSouthWest_thenCorrectBallX() {
        var state = stateBallInMidAndPaddleMid_HEAD_SOUTH(settings.yMid(), HEAD_SOUTH_EAST);
        var res = calculator.calculate(state);
        assertTrue(settings.xMid()<res.xBall());
    }

    @Test
    void whenHeadSouthWestAndSouth_thenSELongerTime() {
        var stateSE = stateBallInMidAndPaddleMid_HEAD_SOUTH(settings.yMid(), HEAD_SOUTH_EAST);
        var stateS = stateBallInMidAndPaddleMid_HEAD_SOUTH(settings.yMid(),HEAD_SOUTH);
        var resSE = calculator.calculate(stateSE);
        var resS = calculator.calculate(stateS);
        assertTrue(resSE.timeHit()>resS.timeHit());
    }


    @Test
    void whenLongerToBottom_moreTime() {
        var state = stateBallInMidAndPaddleMid_HEAD_SOUTH(settings.yMid(), HEAD_SOUTH);
        double time = calculator.calculate(state).timeHit();
        state = stateBallInMidAndPaddleMid_HEAD_SOUTH(settings.yMaxMinusHalfRadiusBall(), HEAD_SOUTH);
        double timeLonger = calculator.calculate(state).timeHit();
        assertTrue(timeLonger > time);
    }

    private
    StatePong stateBallInMidAndPaddleMid_HEAD_SOUTH(double y, double headSouth) {
        return StatePong.of(
                PosXy.of(settings.xMid(), y),
                settings.xMid(),
                headSouth,
                STILL_FOR_A_WHILE);
    }


}
