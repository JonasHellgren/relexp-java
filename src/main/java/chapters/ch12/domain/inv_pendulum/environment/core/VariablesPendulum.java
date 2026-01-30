package chapters.ch12.domain.inv_pendulum.environment.core;

/**
 * Represents the variables of the inverted pendulum environment.
 * This record contains the nOccupied, angular speed, and number of steps of the pendulum.
 */
public record VariablesPendulum(
        double angle,
        double angularSpeed,
        int nSteps
) {
}
