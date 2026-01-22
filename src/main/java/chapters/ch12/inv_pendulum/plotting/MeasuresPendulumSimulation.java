package chapters.ch12.inv_pendulum.plotting;

import chapters.ch12.inv_pendulum.domain.environment.core.ActionPendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.StatePendulum;
import chapters.ch12.inv_pendulum.domain.environment.param.PendulumParameters;
import core.foundation.util.unit_converter.UnitConverterUtil;
import lombok.Builder;

@Builder
public record MeasuresPendulumSimulation(
        double time,
        double angle,
        double angularSpeed,
        double torque,
        double maxAngle
) {

    public static MeasuresPendulumSimulation of(StatePendulum state,
                                                ActionPendulum action,
                                                PendulumParameters parameters) {
        return MeasuresPendulumSimulation.builder()
                .time(state.nSteps()* parameters.dt())
                .angle(state.angle())
                .angularSpeed(state.angularSpeed())
                .torque(action.torque())
                .maxAngle(parameters.angleMax())
                .build();
    }

    double timeInMillis() {
        return time * 1000;
    }

    public Double angleInDegrees() {
        return radToDeg(angle);
    }

    public Double angularSpeedInDegPerSec() {
        return radToDeg(angularSpeed);
    }


    public Double maxAngleDeg() {
        return radToDeg(maxAngle);
    }

    public Double torquex10() {
        return torque * 10;
    }

    double radToDeg(double angleRad) {
        return UnitConverterUtil.convertRadiansToDegrees(angleRad);
    }

}
