package core.foundation.gadget.math;

public record MeanAndLogStd(
        double mean,
        double logStd
) {

    public static MeanAndLogStd of(double mean, double logStd) {
        return new MeanAndLogStd(mean, logStd);
    }

}
