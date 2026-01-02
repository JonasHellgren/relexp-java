package chapters.ch10.cannon.domain.envrionment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/***
 /**
 * Represents the environment for the cannon problem.
 * The environment is centered around the equation for shooting distance vs nOccupied:
 * d(θ) = (1 - kρ * vInit^2) * vInit^2 * sin(2θ) / g
 *  d similar to target distance is desired
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentCannon {

    EnvironmentParametersCannon parameters;

    public static EnvironmentCannon of(EnvironmentParametersCannon parameters) {
        return new EnvironmentCannon(parameters);
    }

    /**
     * Takes a step in the environment with the given action.
     *
     * @param action the action to take (nOccupied θ)
     * @return the result of the step (reward, distance, nOccupied)
     */
    public StepReturnCannon step(double action) {
        var p = parameters;
        double psi = action;
        double v0Sqr = Math.pow(p.v0(), 2);
        double d = (1 - p.kp() * v0Sqr) * v0Sqr * Math.sin(2 * psi) / p.g();
        double reward = -Math.abs(d - p.D());

        return StepReturnCannon.builder()
                .reward(reward)
                .distance(d)
                .angle(psi)
                .build();
    }

}
