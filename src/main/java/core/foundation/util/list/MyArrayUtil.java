package core.foundation.util.list;

import com.google.common.base.Preconditions;
import core.foundation.util.math.MyMathUtils;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Utility class for handling arrays.
 */
public class MyArrayUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private MyArrayUtil() {
    }

    /**
     * Calculate the sum of all elements in the array.
     *
     * @param array The array of numbers.
     * @return The sum of the numbers.
     */
    public static double sum(double[] array) {
        return Arrays.stream(array).sum();
    }


    /**
     * Calculate the average of all elements in the array.
     *
     * @param array The array of numbers.
     * @return The average of the numbers.
     * @throws IllegalArgumentException If the array is null or empty.
     */
    public static double average(double[] array) {
        Preconditions.checkArgument(array != null && array.length > 0,
                "Array must not be null or empty");

        double sum = 0.0;
        for (double num : array) {
            sum += num;
        }

        return sum / array.length;
    }


    /**
     * Finds the minimum value in a given array of doubles.
     *
     * @param array The array of doubles.
     * @return The minimum value in the array.
     * @throws IllegalArgumentException if the array is empty.
     */
    public static double findMin(double[] array) {
        // Check if the array is empty
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        // Initialize the minimum value with the first element of the array
        double min = array[0];
        // Iterate over the array to find the minimum value
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        // Return the minimum value
        return min;
    }

    /**
     * Finds the maximum value in a given array of doubles.
     *
     * @param array The array of doubles.
     * @return The maximum value in the array.
     * @throws IllegalArgumentException if the array is empty.
     */
    public static double findMax(double[] array) {
        // Check if the array is empty
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        // Initialize the maximum value with the first element of the array
        double max = array[0];
        // Iterate over the array to find the maximum value
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        // Return the maximum value
        return max;
    }


    /**
     * Clip the values in the array between the given minimum and maximum values.
     *
     * @param x The array of numbers.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return The clipped array.
     */
    public static double[] clip(double[] x, double min, double max) {
        for (int i = 0; i < x.length; i += 1) {
            x[i] = MyMathUtils.clip(x[i], min, max);
        }
        return x;
    }

    /**
     * Check if two arrays of Doubles are equal within a given tolerance.
     *
     * @param x The first array of Doubles.
     * @param y The second array of Doubles.
     * @param tol The tolerance for equality.
     * @return True if the arrays are equal within the tolerance, false otherwise.
     */
    public static boolean isDoubleArraysEqual(double[] x, double[] y, double tol) {
        if (x.length != y.length) {
            return false;
        }
        for (int i = 0; i < x.length; i += 1) {
            if (Math.abs((y[i] - x[i])) > tol) {
                return false;
            }
        }
        return true;
    }

    /**
     * Multiply each element in the array by a given value.
     *
     * @param x The array of numbers.
     * @param multiplier The value to multiply each element by.
     * @return The array with each element multiplied by the given value.
     */
    public static double[] multiplyWithValue(double[] x, double multiplier) {
        return DoubleStream.of(x)
                .map(v -> v * multiplier)
                .toArray();
    }

    /**
     * Negate each element in the array.
     *
     * @param array The array of numbers.
     * @return The array with each element negated.
     */
    public static double[] negate(double[] array) {
        double[] negatedArray = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            negatedArray[i] = -array[i];  // Negate each element
        }
        return negatedArray;
    }

    /**
     * Convert a map of Doubles to an array of Doubles.
     *
     * @param map The map of Doubles.
     * @return A Pair containing the sorted x values and the corresponding y values.
     */
    public static Pair<double[], double[]> convertMapToArrays(Map<Double, Double> map) {
        List<Double> xList = new ArrayList<>(map.keySet());
        Collections.sort(xList);

        List<Double> yList = new ArrayList<>();
        for (Double x : xList) {
            yList.add(map.get(x));  // Arrange y values according to the sorted x values
        }

        double[] xArray = xList.stream().mapToDouble(Double::doubleValue).toArray();
        double[] yArray = yList.stream().mapToDouble(Double::doubleValue).toArray();
        return Pair.create(xArray, yArray);
    }

    /**
     * Finds the minimum value in a primitive double array.
     *
     * @param minInputValues the input array of double values
     * @return the minimum value in the array
     * @throws IllegalArgumentException if the input array is null or empty
     */
    public static double findMinInPrimitiveArray(double[] minInputValues) {
        if (minInputValues == null || minInputValues.length == 0) {
            throw new IllegalArgumentException("Input array is null or empty");
        }
        return Arrays.stream(minInputValues).min().getAsDouble();
    }

    /**
     * Finds the maximum value in a primitive double array.
     *
     * @param minInputValues the input array of double values
     * @return the maximum value in the array
     * @throws IllegalArgumentException if the input array is null or empty
     */
    public static double findMaxInPrimitiveArray(double[] minInputValues) {
        if (minInputValues == null || minInputValues.length == 0) {
            throw new IllegalArgumentException("Input array is null or empty");
        }
        return Arrays.stream(minInputValues).max().getAsDouble();
    }

    /**
     * Converts an array of doubles to an array of integers.
     *
     * @param xData The array of doubles to convert.
     * @return The array of integers.
     */
    public static int[] doubleToInteger(double[] xData) {
        int[] result = new int[xData.length];
        for (int i = 0; i < xData.length; i++) {
            result[i] = (int) xData[i];
        }
        return result;
    }

    /**
     * Adds two arrays of doubles element-wise.
     *
     * This method takes two arrays of doubles as input, adds corresponding elements together,
     * and returns the resulting array.
     *
     * @param array1 the first array of doubles
     * @param array2 the second array of doubles
     * @return the resulting array of doubles
     * @throws NullPointerException if either array is null
     * @throws ArrayIndexOutOfBoundsException if the arrays are not the same length
     */
    public static double[] add(double[] array1, double[] array2) {
        if (array1.length != array2.length) {
            throw new ArrayIndexOutOfBoundsException("Arrays must be of the same length");
        }
        return IntStream.range(0, array1.length)
                .mapToDouble(i -> array1[i] + array2[i])
                .toArray();
    }

}