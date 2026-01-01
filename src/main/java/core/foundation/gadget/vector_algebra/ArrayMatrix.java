package core.foundation.gadget.vector_algebra;

import org.apache.commons.math3.util.Pair;

public class ArrayMatrix {

    private ArrayMatrix() {
    }
    /**
     * Transposes a given 2D matrix.
     *
     * @param matrix the input matrix to be transposed
     * @return the transposed matrix
     */
    public static int[][] transposeMatrix(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return new int[0][0];
        }
        int numRows = matrix.length; // Original number of rows
        int numCols = matrix[0].length; // Original number of columns
        int[][] transposedMatrix = new int[numCols][numRows];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                transposedMatrix[j][i] = matrix[i][j]; // Swap the row and column indices for the transposition
            }
        }
        return transposedMatrix;
    }

    public static double[][] transposeMatrix(double[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return new double[0][0];
        }
        int numRows = matrix.length; // Original number of rows
        int numCols = matrix[0].length; // Original number of columns
        double[][] transposedMatrix = new double[numCols][numRows];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                transposedMatrix[j][i] = matrix[i][j]; // Swap the row and column indices for the transposition
            }
        }
        return transposedMatrix;
    }

    /**
     * Returns the dimensions of a given 2D matrix.
     *
     * @param data the input matrix
     * @return a Pair containing the number of rows and columns in the matrix
     */
    public static Pair<Integer, Integer> getDimensions(int[][] data) {
        // Check for a null or empty array to avoid NullPointerException
        if (data == null || data.length == 0 || data[0].length == 0) {
            return new Pair<>(0, 0); // Assuming you want to return (0, 0) for an empty or null array
        }
        int numberOfRows = data.length;
        int numberOfColumns = data[0].length; // Assuming a rectangular matrix
        return new Pair<>(numberOfRows, numberOfColumns);
    }

    public static Pair<Integer, Integer> getDimensions(double[][] data) {
        var intData = doubleToInt(data);
        return getDimensions(intData);
    }

    /**
     * Gets the dimensions of a 2D array.
     *
     * @param data the 2D array
     * @return a pair containing the number of rows and columns
     */
    public static Pair<Integer, Integer> getDimensions(String[][] data) {
        // Check if the input array is null or empty
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Input array is empty");
        }
        int rows = data.length;
        int cols = data[0].length;
        return new Pair<>(rows, cols);
    }



        public static int[][] doubleToInt(double[][] data) {
        int[][] intData = new int[data.length][data[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                intData[i][j] = (int) data[i][j];
            }
        }
        return intData;
    }



}
