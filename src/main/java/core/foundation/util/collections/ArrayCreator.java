package core.foundation.util.collections;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.DoubleStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayCreator {

    /**
     * Creates an array of a specified length with all elements having the same double value.
     *
     * @param length the length of the array to be created
     * @param value  the double value to be filled in the array
     * @return an array of doubles with all elements having the same value
     */

    public static double[] createArrayWithSameDoubleNumber(int length, double value) {
        double[] array = new double[length];
        Arrays.fill(array, value);
        return array;
    }

    /**
     * Creates an array of a specified length with elements ranging from a start value to an end value.
     *
     * @param length the length of the array to be created
     * @param start  the starting value of the range
     * @param end    the ending value of the range
     * @return an array of doubles with elements ranging from start to end
     */

    public static double[] createArrayFromStartAndEnd(int length, double start, double end) {
        Preconditions.checkArgument(length > 0);
        double[] array = new double[length];

        if (length == 1) {
            array[0] = start;
            return array;
        }

        double increment = (end - start) / (length - 1);
        for (int i = 0; i < length; i++) {
            array[i] = start + i * increment;
        }
        return array;
    }

    public static double[] createArrayFromStartAndEndWithNofItems(double start, double end, int length) {
        Preconditions.checkArgument(length > 0);
        return createArrayFromStartAndEnd(length, start, end);
    }


    /**
     * Creates an array with elements ranging from a start value to an end value with a specified step.
     *
     * @param start the starting value of the range
     * @param step  the step between elements
     * @param end   the ending value of the range
     * @return an array of doubles with elements ranging from start to end with the specified step
     */

    public static double[] createArrayInRange(double start, double step, double end) {
        long count = (long) Math.ceil((end - start) / step) + 1;
        return DoubleStream.iterate(start, n -> n + step).limit(count).toArray();
    }


    /**
     * Creates an array of a specified length with elements starting from a specified value and incrementing by a specified amount.
     *length=3, start=0, increment=0.5 => array = [0.0, 0.5, 1.0]
     *
     * @param length  the length of the array to be created
     * @param start   the starting value of the array
     * @param increment the increment between elements
     * @return an array of doubles with elements starting from start and incrementing by increment
     */

    public static double[] createArrayFromIncrement(int length, double start, double increment) {
        Preconditions.checkArgument(length > 0);
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            array[i] = start + i * increment;
        }
        return array;
    }


    /**
     * Creates an array with elements starting from a specified start value, ending at a specified end value, and incrementing by a specified amount.
     start=0, end=1, increment=0.5 => array = [0.0, 0.5, 1.0]
     double rest=(end-start) % increment;
     *
     * @param start   the starting value of the array
     * @param end     the ending value of the array
     * @param increment the increment between elements
     * @return an array of doubles with elements starting from start, ending at end, and incrementing by increment
     */

    public static double[] createArrayFromStartEndAndIncrement(double start, double end, double increment) {
        int length = (int) ((end - start) / increment) + 1;

        Preconditions.checkArgument(length > 0);
        BigDecimal remainder = BigDecimal.valueOf(end - start).remainder(BigDecimal.valueOf(increment));
        throwIfLargeRest(remainder.doubleValue());
        return createArrayFromIncrement(length, start, increment);
    }

    public static final double REST_TOL = 1e-3;

    private static void throwIfLargeRest(double rest) {
        if (Math.abs(rest) > REST_TOL) {
            throw new IllegalArgumentException("start, end and increment gives a rest, rest=" + rest);
        }
    }

    /**
     * Creates a new array by duplicating the input array 'n' times.
     *
     * @param arr the input array to be duplicated
     * @param n   the number of times to duplicate the array
     * @return a new array with the input array duplicated 'n' times
     */
    public static double[] duplicate(double[] arr, int n) {
        double[] result = new double[arr.length * n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(arr, 0, result, i * arr.length, arr.length);
        }
        return result;
    }
}
