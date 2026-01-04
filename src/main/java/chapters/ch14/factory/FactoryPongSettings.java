package chapters.ch14.factory;

import chapters.ch14.environments.pong.PongSettings;
import lombok.experimental.UtilityClass;

/***
 * Pong environment settings
 */
@UtilityClass
public class FactoryPongSettings {

    public static PongSettings create() {

        return PongSettings
                .builder()
                .xMax(1)
                .yMax(1)
                .radiusBall(0.01)
                .lengthPaddle(0.2)
                .speedPaddleMax(1)
                .speedBall(1)
                .minHeadingAngleRad(Math.PI / 4)  //45 degrees
                .timeStep(0.1)
                .penMove(-1).penFail(-100)
                .maxTime(100)
                .build();
    }


}
