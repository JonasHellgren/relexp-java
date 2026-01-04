package chapters.ch12.inv_pendulum.domain.environment.core;

import core.foundation.util.rand.RandUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
/**
 * Enum representing the possible actions in inverted pendulum environment.
 * Each action is associated with a GridActionProperties object, which defines the
 * torque and an arrow symbol.
 */

@Getter
@AllArgsConstructor
public enum ActionPendulum {
    CCW(PendulumActionProperties.of(-2,"←")),
    N(PendulumActionProperties.of(0,"0")),
    CW(PendulumActionProperties.of(2,"→"));

    private final PendulumActionProperties properties;

    public static List<ActionPendulum> getAllActions() {
        return List.of(ActionPendulum.CCW, ActionPendulum.N, ActionPendulum.CW);
    }

    public static int maxNofActions() {
        return getAllActions().size();
    }

    public static ActionPendulum random() {
        return getAllActions().get(RandUtils.getRandomIntNumber(0, maxNofActions()));
    }

    public double torque() {return properties.torque();}

    @Override
    public String toString() {
        return properties.arrow();
    }

}
