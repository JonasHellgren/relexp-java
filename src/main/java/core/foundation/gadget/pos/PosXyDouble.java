package core.foundation.gadget.pos;

import core.foundation.util.formatting.NumberFormatterUtil;
import core.foundation.util.math.MathUtil;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a 2D position with double coordinates.
 */
public record PosXyDouble(
        double x,
        double y
) {

    public static final double INF = Double.MAX_VALUE;
    public static final double EPS = Double.MIN_VALUE;

    public static PosXyDouble of(double x, double y) {
        return new PosXyDouble(x, y);
    }

    public static PosXyDouble zero() {
        return new PosXyDouble(0, 0);
    }

    public boolean isXExceeds(double xMax) {
        return x > xMax;
    }

    public boolean yExceeds(double yMax) {
        return y > yMax;
    }

    public boolean yBelowOrEqual(double v) {
        return MathUtil.isInLimits(y,-INF, v+ EPS);
    }

    public boolean yAboveOrEqual(double v) {
        return MathUtil.isInLimits(y,v- EPS, INF);
    }

    public boolean xBelowOrEqual(double v) {
        return MathUtil.isInLimits(x,-INF, v+ EPS);
    }

    public boolean xAboveOrEqual(double v) {
        return MathUtil.isInLimits(x,v- EPS, INF);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosXyDouble that = (PosXyDouble) o;
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
