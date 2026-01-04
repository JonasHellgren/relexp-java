package chapters.ch14.environments.pong;

import java.util.ArrayList;
import java.util.List;

/**
 * This enum represents the possible actions that can be taken in the Pong game.
 * Each action is associated with a paddle speed.
 */
public enum ActionPong {
    left(-1d), still(0d),right(+1d);

    /**
     * Paddle speed associated with each action.
     */
    double speedPaddleX;

    ActionPong(double speedPaddleX) {
        this.speedPaddleX = speedPaddleX;
    }

    public static List<ActionPong> actionsMutable() {
        return new ArrayList<>(actions());
    }

    public static List<ActionPong> actions() {
        return List.of(values());
    }

    public double velPaddleX() {
        return speedPaddleX;
    }

    public boolean isStill() {
        return this.equals(still);
    }

    public boolean isMove() {
        return !isStill();
    }

}
