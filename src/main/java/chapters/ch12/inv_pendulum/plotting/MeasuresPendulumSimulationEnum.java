package chapters.ch12.inv_pendulum.plotting;

import lombok.AllArgsConstructor;
import java.util.function.Function;

@AllArgsConstructor
public enum MeasuresPendulumSimulationEnum {
    TIME("Time", pm -> pm.time()),
    TIME_MS("Time", pm -> pm.timeInMillis()),
    ANGLE("Angle", pm -> pm.angle()),
    ANGLE_DEG("Angle", pm -> pm.angleInDegrees()),
    ANGLE_MAX("Angle", pm -> pm.maxAngle()),
    ANGLE_MAX_DEG("Angle", pm -> pm. maxAngleDeg()),
    ANGULAR_SPEED("Angular speed", pm -> pm.angularSpeed()),
    ANGULAR_SPEED_DEG("Angular speed", pm -> pm.angularSpeedInDegPerSec()),
    TORQUE("Torque", pm -> pm.torque()),
    TORQUEx10("Torque", pm -> pm.torquex10());

    public final String description;
    public final Function<MeasuresPendulumSimulation, Double> mapFunction;


    }
