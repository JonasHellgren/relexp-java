package chapters.ch11.domain.environment.param;

import core.foundation.util.math.MathUtil;
import lombok.Builder;
import lombok.With;

import java.util.List;

import static core.foundation.util.collections.ListCreatorUtil.createFromStartToEndWithNofItems;


/**
 * Represents the parameters for the Lunar Lander environment.
 * These parameters define, for example, the physical properties of the lander and the environment.
 * <p>
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

    /**
     * Returns a list of evenly spaced values representing the height of the lander.
     *
     * @param nItems the number of items in the list.
     * @return a list of double values representing the height of the lander.
     */
    public List<Double> ySpace(int nItems) {
        return createFromStartToEndWithNofItems(ySurface(), yMax() + TOL, nItems);
    }

    /**
     * Returns a list of evenly spaced values representing the speed of the lander.
     *
     * @param nItems the number of items in the list.
     * @return a list of double values representing the speed of the lander.
     */
    public List<Double> spdSpace(int nItems) {
        return createFromStartToEndWithNofItems(-spdMax(), spdMax() + TOL, nItems);
    }

    /**
     * Clips the input force to the range [forceMin, forceMax].
     *
     * @param forceInNewton the input force in Newtons.
     * @return the clipped force in kN.
     */
    public double clippedForce(double forceInNewton) {
        return MathUtil.clip(forceInNewton, forceMin(), forceMax());
    }
}
