package chapters.ch13.factory.lane_change;

import chapters.ch13.implem.lane_change.LaneChangeParameters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LaneChangeParametersFactory {

    public static LaneChangeParameters produce() {
        return LaneChangeParameters.builder()
                .wheelBase(2.5).speed(20)
                .timeMax(3)
                .yPosDesired(-3).yPosDesiredMargin(0.5).yPosDitch(-5)
                .rFail(-10).rChangeSteering(-1.0).rCorrectYPos(1)
                .timeStep(0.25)
                .build();
    }

}
