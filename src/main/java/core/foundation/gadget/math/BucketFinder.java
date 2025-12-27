package core.foundation.gadget.math;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Optional;

/**
 * BucketFinder:
 *
 * A class to divide a continuous range into discrete buckets, where each bucket corresponds to a fixed
 * size interval. Values outside the range return an empty result.
 *
 * Example:
 *
 * Consider a range [0, 2) divided into buckets of size 1:
 *
 *       Range: [0, 2)
 *       Buckets: [0, 1) -> Bucket 0
 *                [1, 2) -> Bucket 1
 *
 * Values falling into these intervals will be assigned bucket indices:
 *
 *     Input Value       | Bucket Index
 *     ------------------|-------------
 *     0.5               | 0
 *     1.2               | 1
 *     2.0               | (Outside range -> Empty)
 *     -0.5              | (Outside range -> Empty)
 *
 * ASCII Visualization of Buckets:
 *
 *    |----------|----------|
 *    0          1          2
 *    ^ Bucket 0 ^ Bucket 1 ^
 *
 * Each bucket size is determined by the given `bucketSize`, and the range is defined by a `Range` object.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BucketFinder {

    private Range<Double> range;
    private double bucketSize;

    public static BucketFinder of(Range<Double>  range, double bucketSize) {
        Preconditions.checkArgument(bucketSize > 0, "Bucket size must be greater than 0");
        return new BucketFinder(range, bucketSize);
    }

    /**
     * Determines the bucket index for a given value.
     *
     * @param value The input value to map into a bucket.
     * @return An Optional containing the bucket index, or an empty Optional if the value is outside the range.
     */
    public Optional<Integer> getBucket(double value) {
        if (!range.contains(value)) {
            return Optional.empty(); // Value is outside the range
        }
        int bucketIndex = (int) ((value - range.lowerEndpoint()) / bucketSize);
        return Optional.of(bucketIndex);
    }

    /**
     * Determines the bucket index for a value adjusted half a bucket size above.
     *
     * @param value The input value to adjust and map into a bucket.
     * @return An Optional containing the bucket index, or an empty Optional if the adjusted value is outside the range.
     */
    public Optional<Integer> getBucketHalfBucketSizeAbove(double value) {
        double adjustedValue = value + (bucketSize / 2);
        if (!range.contains(adjustedValue)) {
            return Optional.empty(); // Adjusted value is outside the range
        }

        int bucketIndex = (int) ((adjustedValue - range.lowerEndpoint()) / bucketSize);
        return Optional.of(bucketIndex);
    }


}
