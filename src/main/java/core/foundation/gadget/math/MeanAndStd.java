package core.foundation.gadget.math;

import com.google.common.base.Preconditions;

/**
 * Represents a mean and standard deviation pair.
 * This record class is used to store  mean and standard deviation values.
 */

public record MeanAndStd(
        double mean,
        double std
) {

    public static MeanAndStd of(double mean, double std) {
        Preconditions.checkArgument(std >= 0,"std must be non-negative");
        return new MeanAndStd(mean, std);
    }

}
