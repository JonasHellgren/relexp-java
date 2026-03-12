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
        return line(x1, y1, x2, y2, Color.BLACK, 2f);
    }


    public static LineSegment grey(double x1, double y1, double x2, double y2) {
        return line(x1, y1, x2, y2, Color.GRAY, 2f);
    }

    public static LineSegment blackBold(double x1, double y1, double x2, double y2) {
        return line(x1, y1, x2, y2, Color.BLACK, 15f);
    }

    public static LineSegment redBold(double x1, double y1, double x2, double y2) {
        return line(x1, y1, x2, y2, Color.RED, 15f);
    }

    public static LineSegment goldSmall(double x1, double y1) {
        return circleSmall(x1, y1, Color.YELLOW);
    }

    public static LineSegment circleSmall(double x1, double y1, Color yellow) {
        return line(x1, y1, x1, y1, yellow, 5f);
    }

    public static LineSegment goldBig(double x1, double y1) {
        return circle(x1, y1, Color.YELLOW);
    }

    public static LineSegment circle(double x1, double y1, Color color) {
        return circleCommon(x1, y1, color, 15f);
    }


    public static LineSegment circleCommon(double x1, double y1, Color color, float t) {
        return line(x1, y1, x1, y1, color, t);
    }

    public static LineSegment line(double x1, double y1, double x2, double y2, Color red, float thickness1) {
        return new LineSegment(x1, y1, x2, y2, thickness1, END_ROUNDING, JOINING, red);
    }

}
