package chapters.ch12.inv_pendulum.domain.environment.param;

import core.foundation.util.collections.ListCreatorUtil;
import core.foundation.util.unit_converter.UnitConverterUtil;
import lombok.Builder;
import lombok.With;
import java.util.List;

/**
 * Represents the parameters of the inverted pendulum environment.
 * These parameters define the physical properties of the environment.
 */
@Builder
@With
public record PendulumParameters(
        double inertia,  //Rod inertia (kg/m2)
        double length,  //Rod length (m)
        double m,  //Rod mass (kg)
        double dt,  //Time step (s)
        double g,  //Constant of gravity on the earth. (m/s2)
        double rewardFail,  //Reward for fail action
        double rewardStep,  //Reward for each step
        double angleMax,  //Magnitude of maximum allowed nOccupied (rad)
        double angleSpeedMax,  //Magnitude of guessed maximum nOccupied sped (rad/s)
        double lambdaEnLoss,  //lambda for energy loss
        double lambdaFail,  //lambda for fail
        double maxTime  //maximum time in epis
)

{
    public int maxSteps() {
        return (int) (maxTime / dt);
    }

    double radToDeg(double angleRad) {
        return UnitConverterUtil.convertRadiansToDegrees(angleRad);
    }

    public List<Double> thetaSpaceInDegrees(int nItems) {
        return ListCreatorUtil.createFromStartToEndWithNofItems(
                -radToDeg(angleMax()), radToDeg(angleMax()), nItems);
    }

    public List<Double> thetaSpace(int nItems) {
        return ListCreatorUtil.createFromStartToEndWithNofItems(-angleMax(), angleMax(), nItems);
    }

    public List<Double> narrowSpdSpace(int nItems) {
        return ListCreatorUtil.createFromStartToEndWithNofItems(-narrowSpd(), narrowSpd(), nItems);
    }

    public List<Double> narrowSpdSpaceInDegPerSec(int nColRowsHeatMap) {
        return ListCreatorUtil.createFromStartToEndWithNofItems(-radToDeg(narrowSpd()), radToDeg(narrowSpd()), nColRowsHeatMap);
    }


    private double narrowSpd() {
        return angleSpeedMax()/4;
    }

}
