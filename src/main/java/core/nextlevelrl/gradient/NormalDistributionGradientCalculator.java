package core.nextlevelrl.gradient;

import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

import static core.foundation.util.math.FunctionsUtil.sqr2;

/**
 * This class provides methods for calculating the gradient of a normal distribution with respect to its mean
 * and standard deviation.
 * The gradient is a measure of how the distribution changes when the mean or standard deviation is varied.
 * The gradients returned by the gradient method can be used in optimization algorithms to update the mean and
 * standard deviation of the normal distribution. The optimization algorithm will typically adjust the mean and
 * standard deviation in the direction of the negative gradient to minimize the loss function.
 */

@AllArgsConstructor
public final class NormalDistributionGradientCalculator {

    public static final double SMALLEST_DENOM = 1e-2;

    double smallestDenom;

    public NormalDistributionGradientCalculator() {
        this(SMALLEST_DENOM);
    }

    public static NormalDistributionGradientCalculator of(double smallestDenom) {
        return new NormalDistributionGradientCalculator(smallestDenom);
    }

    /**
     * Calculates the gradient of the normal distribution with respect to its mean and standard deviation.
     *
     * @param action the action value
     * @param meanAndStd the mean and standard deviation of the normal distribution
     * @return a pair containing the gradient of the mean and the gradient of the log standard deviation
     */
    public Pair<Double, Double> gradient(double action, Pair<Double, Double> meanAndStd) {
        double mean = meanAndStd.getFirst();
        double std = meanAndStd.getSecond();
        double denom = Math.max(sqr2.apply(std), smallestDenom);
        double gradMean = 1 / denom * (action - mean);
        double gradLogStd = sqr2.apply(action - mean) /denom - 1d;
        return Pair.create(gradMean, gradLogStd);
    }

    public double gradientMean(double action, double mean, double std) {
        double denom = Math.max(sqr2.apply(std), smallestDenom);
        return  1 / denom * (action - mean);
    }

    public double gradientLogStd(double action, double mean, double logStd) {
        double denom = Math.max(Math.exp(2*logStd), smallestDenom);
        return sqr2.apply(action - mean) /denom - 1d;
    }


}
