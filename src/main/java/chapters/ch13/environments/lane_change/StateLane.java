package chapters.ch13.environments.lane_change;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * Represents a state in the lane change environment.
 * This class encapsulates the state variables x, y, headingAngleDot, headingAngle, and time.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateLane {

    private double x;
    private double y;
    private double headingAngleDot;
    private double headingAngle;
    private double time;

    public static StateLane of(double x,
                               double y,
                               double headingAngleDot,
                               double headingAngle) {
        return StateLane.ofWithTime(x, y, headingAngleDot, headingAngle, 0);
    }

    public static StateLane ofWithTime(double x,
                                       double y,
                                       double headingAngleDot,
                                       double headingAngle,
                                       double time) {
        return new StateLane(x, y, headingAngleDot, headingAngle, time);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double headingAngleDot() {
        return headingAngleDot;
    }


    public double headingAngle() {
        return headingAngle;
    }

    public double time() {
        return time;
    }

    public StateLane copy() {
        return ofWithTime(x, y, headingAngleDot, headingAngle, time);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateLane that = (StateLane) o;
        return y == that.y && x == that.x && headingAngle == that.headingAngle;  //skipping time
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.headingAngle);
    }

    @Override
    public String toString() {
        return "StateLane{" +
                "x=" + x +
                ", y=" + y +
                ", headingAngle=" + headingAngle +
                ", headingAngleDot=" + headingAngleDot +
                ", time=" + time +
                '}';
    }


}
