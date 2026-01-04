package chapters.ch14.environments.pong;

import core.foundation.util.formatting.NumberFormatterUtil;
import core.foundation.util.math.MyMathUtils;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a 2D position with double coordinates.
 */
public record PosXy(
        double x,
        double y
) {

    public static final double INF = Double.MAX_VALUE;
    public static final double EPS = Double.MIN_VALUE;

    public static PosXy of(double x, double y) {
        return new PosXy(x, y);
    }

    public static PosXy zero() {
        return new PosXy(0, 0);
    }

    public boolean isXExceeds(double xMax) {
        return x > xMax;
    }

    public boolean yExceeds(double yMax) {
        return y > yMax;
    }

    public boolean yBelowOrEqual(double v) {
        return MyMathUtils.isInLimits(y,-INF, v+ EPS);
    }

    public boolean yAboveOrEqual(double v) {
        return MyMathUtils.isInLimits(y,v- EPS, INF);
    }

    public boolean xBelowOrEqual(double v) {
        return MyMathUtils.isInLimits(x,-INF, v+ EPS);
    }

    public boolean xAboveOrEqual(double v) {
        return MyMathUtils.isInLimits(x,v- EPS, INF);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosXy that = (PosXy) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }

    @Override
    public String toString() {
        return "(x,y)=("+rounded(x)+","+rounded(y)+")";
    }

    @NotNull
    private String rounded(double num) {
        return NumberFormatterUtil.getRoundedNumberAsString(num, 2);
    }


}
