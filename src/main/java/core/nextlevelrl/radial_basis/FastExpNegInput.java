package core.nextlevelrl.radial_basis;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Fast approximation of exp(x) for x <= 0 using a lookup table.
 * <p>
 * Idea:
 * - Precompute values for exp(x) for x in [xMin, 0], where xMin = ln(eps)
 * - eps is the cutoff: exp(xMin) = eps (≈ 0)
 * - At runtime, quantize x to an index and do one table lookup.
 * <p>
 * Trade-off:
 * - More entries  -> better accuracy, more memory
 * - Fewer entries -> faster, less memory, more approximation error
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FastExpNegInput {

    private final double eps;
    private final double xMin;     // <= 0, exp(xMin) = eps
    private final double invStep;
    private final double[] lut;    // lut[i] ≈ exp(x_i)

    /**
     * @param eps  cutoff: exp(xMin) = eps (e.g. 1e-4). Must be in (0, 1).
     * @param size number of table entries (e.g. 512, 1024, 2048)
     */
    public static FastExpNegInput of(double eps, int size) {
        Preconditions.checkArgument(eps > 0.0 && eps < 1.0, "eps must be in (0, 1)");
        Preconditions.checkArgument(size >= 2, "size must be >= 2");
        // xMin such that exp(xMin) = eps  =>  xMin = ln(eps)
        double xMin = Math.log(eps);   // negative
            double[] lut = new double[size];
        double step = (0.0 - xMin) / (size - 1);
        double invStep = 1.0 / step;
        for (int i = 0; i < size; i++) {
            double x = xMin + i * step;
            lut[i] = Math.exp(x);    // exact here, used only at construction
        }
        return new FastExpNegInput(eps, xMin, invStep, lut);
    }

    // Optional helper with sane defaults
    public static FastExpNegInput createDefault() {
        return of(1e-4, 1024);
    }

    /**
     * Fast approximation of exp(x) for x <= 0.
     *
     * @param x exponent (typically <= 0)
     * @return approximated exp(x)
     */
    public double fastExp(double x) {
        if (x >= 0.0) {
            return 1.0;         // exp(0) = 1, clamp anything above 0
        }
        if (x <= xMin) {
            return 0.0;         // exp(x) <= eps, treat as zero
        }

        // Map x from [xMin, 0] to index [0, lut.length-1]
        double t = (x - xMin) * invStep;   // 0 .. size-1
        int idx = (int) t;

        // Safety clamp (cheap)
        if (idx < 0) {
            idx = 0;
        } else if (idx >= lut.length) {
            idx = lut.length - 1;
        }

        return lut[idx];
    }


}
