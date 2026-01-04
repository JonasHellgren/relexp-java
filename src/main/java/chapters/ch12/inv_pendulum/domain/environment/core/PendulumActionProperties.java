package chapters.ch12.inv_pendulum.domain.environment.core;

/**
 * Represents the properties of an action in inverted pendulum environment, supports ActionGrid enum
 * An action consists of the torque, and an arrow symbol.
 */
public record PendulumActionProperties(
        double torque,
        String arrow
) {

    public static PendulumActionProperties of(double torque, String arrow) {
        return new PendulumActionProperties(torque,arrow);
    }

}
