package core.foundation.gadget.math;


/**
 * This record class is used to store parameters representing mean and standard deviation values.
 */


public record MeanAndStdMemoryParameters(
        double zMean,
        double zStd
) {

    public static MeanAndStdMemoryParameters of(double zm, double zs) {
        return new MeanAndStdMemoryParameters(zm, zs);
    }

}
