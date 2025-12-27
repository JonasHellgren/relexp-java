package core.foundation.util.list;

import java.util.ArrayList;
import java.util.List;

public class Array2ListConverter {

    private Array2ListConverter() {
    }

    /**
     * Converts a double array to a list of doubles.
     *
     * @param inputArray the input array to convert
     * @return a list of doubles
     */
    public static List<Double> arrayToList(double[] inputArray) {
        List<Double> outputList = new ArrayList<>(inputArray.length);
        for (double value : inputArray) {
            outputList.add(value);
        }
        return outputList;
    }

    /**
     * Converts a 2D double array to a list of lists of doubles.
     *
     * @param inputMatrix the input matrix to convert
     * @return a list of lists of doubles
     */
    public static List<List<Double>> matrixToListOfLists(double[][] inputMatrix) {
        List<List<Double>> outputList = new ArrayList<>();
        for (double[] row : inputMatrix) {
            List<Double> innerList = new ArrayList<>();
            for (double value : row) {
                innerList.add(value);
            }
            outputList.add(innerList);
        }
        return outputList;
    }
}