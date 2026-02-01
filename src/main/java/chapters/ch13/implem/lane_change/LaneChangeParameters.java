package chapters.ch13.implem.lane_change;

import lombok.Builder;

import static core.foundation.util.math.MathUtil.isEqualDoubles;

@Builder
public record LaneChangeParameters(
        double wheelBase,
        double speed,  //m/s
        double timeMax,  //s
        double yPosDesired, //m
        double yPosDesiredMargin, //m
        double yPosDitch, //m
        double rFail,
        double rChangeSteering,
        double rCorrectYPos,
        double timeStep
) {


    public boolean isDesiredYPositionReached(StateLane newS) {
        return isEqualDoubles(newS.y(), yPosDesired(), yPosDesiredMargin());
    }


}
