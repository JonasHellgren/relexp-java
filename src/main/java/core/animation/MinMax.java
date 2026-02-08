package core.animation;

public final class MinMax {
    public final double min;
    public final double max;

    public MinMax(double min, double max) {
        this.min = min;
        this.max = (min == max) ? (min + 1.0) : max;
    }

    public static MinMax of(double[][] grid) {
        double mn = Double.POSITIVE_INFINITY;
        double mx = Double.NEGATIVE_INFINITY;
        for (double[] row : grid) {
            for (double v : row) {
                if (v < mn) mn = v;
                if (v > mx) mx = v;
            }
        }
        return new MinMax(mn, mx);
    }
}
