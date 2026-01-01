package core.foundation.util.list_array;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyMatrixListUtils {

    /**
     * Finds the minimum value in a 2D list of doubles.
     *
     * @param data a 2D list of doubles
     * @return the minimum value in the list
     */
    public static double findMin(List<List<Double>> data) {
        return data.stream()
                .flatMap(List::stream)
                .mapToDouble(Double::doubleValue)
                .min()
                .getAsDouble();
    }

    /**
     * Finds the maximum value in a 2D list of doubles.
     *
     * @param data a 2D list of doubles
     * @return the maximum value in the list
     */
    public static double findMax(List<List<Double>> data) {
        return data.stream()
                .flatMap(List::stream)
                .mapToDouble(Double::doubleValue)
                .max()
                .getAsDouble();
    }

}
