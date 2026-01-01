package core.foundation.gadget.normal_distribution;

import org.apache.commons.math3.util.Pair;

public class ProbabilityDistributionFunction {

    public static  double pdf(double x, Pair<Double,Double> meanAndStd) {
        return pdf(x,meanAndStd.getFirst(),meanAndStd.getSecond());
    }

    public static  double pdf(double x, double m, double s) {
        return (1 / (s * Math.sqrt(2 * Math.PI))) *
                Math.exp(-0.5 * Math.pow((x - m) / s, 2));
    }
}
