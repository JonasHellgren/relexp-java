package core.animation;

import java.awt.*;

public record LineSegment(
        double x1, double y1,
        double x2, double y2,
        float thickness,
        int endRounding,
        int joining,
        Color color
) {

    public static final int JOINING = BasicStroke.JOIN_ROUND;
    public static final int END_ROUNDING = BasicStroke.CAP_ROUND;

    public static LineSegment black(double x1, double y1, double x2, double y2) {
        return new LineSegment(x1, y1, x2, y2, 2f, END_ROUNDING, JOINING, Color.BLACK);
    }

    public static LineSegment red(double x1, double y1, double x2, double y2) {
        return new LineSegment(x1, y1, x2, y2, 2f, END_ROUNDING, JOINING, Color.RED);
    }

    public static LineSegment blackBold(double x1, double y1, double x2, double y2) {
        return new LineSegment(x1, y1, x2, y2, 15f, END_ROUNDING, JOINING, Color.BLACK);
    }

    public static LineSegment redBold(double x1, double y1, double x2, double y2) {
        return new LineSegment(x1, y1, x2, y2, 15f, END_ROUNDING, JOINING, Color.RED);
    }
}
