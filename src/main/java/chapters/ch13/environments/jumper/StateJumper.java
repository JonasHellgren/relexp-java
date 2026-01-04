package chapters.ch13.environments.jumper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateJumper {

    int height;
    int xPos;

    public static StateJumper zeroHeight() {
        return StateJumper.of(0,0);
    }


    public static StateJumper ofHeight(int height) {
        return new StateJumper(height,0);
    }


    public static StateJumper of(int height, int time) {
        return new StateJumper(height,time);
    }

    public StateJumper createNext(ActionJumper action) {
        return new StateJumper(height + action.delta, xPos + 1);
    }

    public StateJumper createNextSameHeight() {
        return new StateJumper(height, xPos + 1);
    }

    public int height() {
        return height;
    }

    public int x() {
        return xPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateJumper that = (StateJumper) o;
        return height == that.height && xPos == that.xPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height);
    }

    @Override
    public String toString() {
        return "h=" + height+ ", x=" + xPos;
    }

}
