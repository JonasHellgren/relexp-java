package core.animation;

import org.jfree.chart.renderer.LookupPaintScale;

import java.awt.*;

public final class PaintScales {
    private PaintScales() {}

    /** 
     * Gråskala: min -> svart, max -> vit
     */
    public static LookupPaintScale grayscale(double min, double max, int steps) {
        LookupPaintScale ps =
                new LookupPaintScale(min, max, Color.BLACK);

        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;          // 0..1
            double v = min + t * (max - min);       // min..max
            ps.add(v, gray(t));
        }
        return ps;
    }

    private static Color gray(double t) {
        t = clamp01(t);
        float g = (float) t;
        return new Color(g, g, g);
    }

    private static double clamp01(double x) {
        return Math.max(0.0, Math.min(1.0, x));
    }
}
