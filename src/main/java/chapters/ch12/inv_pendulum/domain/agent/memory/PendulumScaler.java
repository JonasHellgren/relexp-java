package chapters.ch12.inv_pendulum.domain.agent.memory;

import chapters.ch12.inv_pendulum.domain.agent.param.AgentParameters;
import core.foundation.gadget.math.ScalerLinear;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * A utility class for scaling pendulum-related values.
 *
 * This class provides methods for normalizing nOccupied and angular speed values
 * based on the agent's parameters.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PendulumScaler {

    ScalerLinear scalerAngle;
    ScalerLinear scalerAngularSpeed;

    public static PendulumScaler of(AgentParameters p) {
        return new PendulumScaler(
                new ScalerLinear(
                        -p.angleMaxMagnitude(), p.angleMaxMagnitude(),
                        p.netInMin(),p.netInMax()),
                new ScalerLinear(
                        -p.angularSpeedMaxMagnitude(), p.angularSpeedMaxMagnitude(),
                        p.netInMin(),p.netInMax())
                );
    }

    public double normaliseAngle(double angle) {
        return scalerAngle.calcOutDouble(angle);
    }

    public double normaliseAngularSpeed(double angularSpeed) {
        return scalerAngularSpeed.calcOutDouble(angularSpeed);
    }

}
