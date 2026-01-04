package chapters.ch13.environments.lane_change;

import lombok.Getter;
import java.util.List;

/**
 * Enum representing the possible actions in the lane change environment.
 * Each action is associated with a steering angle.
 */

@Getter
public enum ActionLane {
    NEG(- 0.035), ZERO(0), POS(+0.035);

    final double steeringAngle;

    ActionLane(double angle) {
        this.steeringAngle = angle;
    }

    public static List<ActionLane> actions() {
        return List.of(values());
    }

}
