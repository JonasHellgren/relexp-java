package core.foundation.util.collections;

import lombok.experimental.UtilityClass;
import java.util.Arrays;
import java.util.Objects;

@UtilityClass
public class MyMatrixArrayUtil {

    /**
     * Finds the minimum value in a 2D array of doubles.
     *
     * @param data the 2D array of doubles
     * @return the minimum value in the array
     */
    public static double findMin(double[][] data) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] < min) {
                    min = data[i][j];
                }
            }
        }
        return min;
    }

    /**
     * Finds the maximum value in a 2D array of doubles.
     *
     * @param data the 2D array of doubles
     * @return the maximum value in the array
     */
    public static double findMax(double[][] data) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] > max) {
                    max = data[i][j];
                }
            }
        }
        return max;
    }


    /**
     * Find the minimum value in a two-dimensional array of Doubles.
     *
     * @param data The two-dimensional array of Doubles.
     * @return The minimum value in the array.
     * @throws IllegalArgumentException If the array is null or empty.
     */
    public static Double findMin(Double[][] data) {
        return Arrays.stream(data)
                .flatMap(Arrays::stream)
                .filter(n -> !Objects.isNull(n))
                .min(Double::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("The array must not be null or empty"));
    }

    /**
     * Find the maximum value in a two-dimensional array of Doubles.
     *
     * @param data The two-dimensional array of Doubles.
     * @return The maximum value in the array.
     * @throws IllegalArgumentException If the array is null or empty.
     */
    public static Double findMax(Double[][] data) {
        return Arrays.stream(data)
                .flatMap(Arrays::stream)
                .filter(n -> !Objects.isNull(n))
                .max(Double::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("The array must not be null or empty"));
    }


    /**
     * This method creates and returns a new 2D array of doubles with the specified number of rows and columns.
     *
     * @param nRows The number of rows in the array.
     * @param nCol The number of columns in the array.
     * @return A new 2D array of doubles with the specified number of rows and columns.
     * @throws IllegalArgumentException If either nRows or nCol is null or negative.
     */
    public static double[][] emptyMatrix(Integer nRows, Integer nCol) {
        // Check if the input parameters are valid
        if (nRows == null || nCol == null || nRows < 0 || nCol < 0) {
            throw new IllegalArgumentException("The number of rows and columns must be non-negative integers.");
        }

        // Create and return a new 2D array of doubles with the specified number of rows and columns
        return new double[nRows][nCol];
    }


}
