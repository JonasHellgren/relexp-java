package core.nextlevelrl.gradient;


import core.foundation.gadget.math.MeanAndLogStd;
import core.foundation.util.math.MathUtil;

/**
 * Represents a gradient of the mean and standard deviation of a normal distribution.
 */

public record GradientMeanAndLogStd(MeanAndLogStd meanStd) {

    public static GradientMeanAndLogStd of(double gradMean, double gradLogStd) {
        return new GradientMeanAndLogStd(new MeanAndLogStd(gradMean, gradLogStd));
    }

    /**
     * Clips the gradient mean and standard deviation to the given maximum values.
     *
     * @param meanMax the maximum value for the gradient mean
     * @param logStdMax the maximum value for the gradient standard deviation
     * @return a new GradientMeanStd instance with clipped values
     */

    public GradientMeanAndLogStd clip(double meanMax, double logStdMax) {
        return GradientMeanAndLogStd.of(
                MathUtil.clip(meanStd.mean(), -meanMax, meanMax),
                MathUtil.clip(meanStd.logStd(), -logStdMax, logStdMax));
    }

    public double mean() {
        return meanStd.mean();
    }

    public double std() {
        return meanStd.logStd();
    }
}
