package chapters.ch10.cannon.domain.envrionment;

import lombok.Builder;
import lombok.With;

/**
 * Represents the parameters of the cannon environment.
 * These parameters define the physical properties of the environment.
 */

@Builder
@With
public record EnvironmentParametersCannon(
        double windResistanceCoefficient,  //unit less
        double speedInit,  //unit m/s
        double constantOfGravity,  //unit m/s^2
        double distanceTarget //unit m

) {

    public double kp() {
        return windResistanceCoefficient;
    }

    public double v0() {
        return speedInit;
    }

    public double g() {
        return constantOfGravity;
    }

    public double D() {
        return distanceTarget;
    }


}
