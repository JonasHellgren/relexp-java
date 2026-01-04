package chapters.ch11.domain.environment.param;

import core.foundation.util.math.MyMathUtils;
import lombok.Builder;
import lombok.With;
import java.util.List;

import static core.foundation.util.collections.ListCreator.createFromStartToEndWithNofItems;


/**
 * Represents the parameters for the Lunar Lander environment.
 * These parameters define, for example, the physical properties of the lander and the environment.
 *
 * yMax and spdMax are used to create kernels in agent memory, defines memory boundary
 */

@With
@Builder
public record LunarParameters(
        double massLander,  //kg
        double dt,  //seconds
        double g,  //constant of gravity at moon
        double forceMin,  //The minimum force that can be applied to the lander in kN.
        double forceMax,  //The maximum force that can be applied to the lander in kN.
        double ySurface,  //The minimum height of the lunar surface in meters.
        double yMax,  //The maximum height of the lander in meters.
        double spdMax,  //The maximum absolute speed of the lander in m/s
        double spdLimitCrash,  //The speed limit for crashing in m/s.
        double rewardFail,  //The reward for failing to land safely.
        double rewardSuccess,  //The reward for landing safely.
        double rewardStep) {  //The reward for each step taken during the simulation.

    public static final double TOL = 1e-3;

    public static LunarParameters defaultProps() {
        return LunarParameters.builder()
                .massLander(500.0)
                .dt(0.2)
                .g(1.62)
                .forceMin(0)
                .forceMax(2)
                .ySurface(0d)
                .yMax(5)
                .spdMax(5)
                .spdLimitCrash(1d)
                .rewardFail(-100)
                .rewardSuccess(100)
                .rewardStep(-1)
                .build();
    }

    /**
     * Returns a list of evenly spaced values representing the height of the lander.
     *
     * @param nItems the number of items in the list.
     * @return a list of double values representing the height of the lander.
     */
    public List<Double> ySpace(int nItems) {
        return createFromStartToEndWithNofItems(ySurface(), yMax()+TOL, nItems);
    }

    /**
     * Returns a list of evenly spaced values representing the speed of the lander.
     *
     * @param nItems the number of items in the list.
     * @return a list of double values representing the speed of the lander.
     */
    public List<Double> spdSpace(int nItems) {
        return createFromStartToEndWithNofItems(-spdMax(), spdMax()+ TOL, nItems);
    }

    /**
     * Clips the input force to the range [forceMin, forceMax].
     *
     * @param forceInNewton the input force in Newtons.
     * @return the clipped force in kN.
     */
    public double clippedForce(double forceInNewton) {
        return MyMathUtils.clip(forceInNewton, forceMin(), forceMax());
    }
}
