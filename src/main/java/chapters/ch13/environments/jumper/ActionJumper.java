package chapters.ch13.environments.jumper;

import java.util.List;

/**
 * Enum representing the possible actions in the Climber environment.
 * The possible actions are:
 * <ul>
 *     <li>up: move up by 1 position</li>
 *     <li>n: no movement (stay at the same position)</li>
 * </ul>
 */
public enum ActionJumper {
    up(+1), n(0);


    /**
     * The change in height (delta) associated with each action.
     */
    int delta;

    ActionJumper(int delta) {
        this.delta = delta;
    }

    public static int nActions() {
        return values().length;
    }

    public static List<ActionJumper> actions() {
        return List.of(values());
    }
}
