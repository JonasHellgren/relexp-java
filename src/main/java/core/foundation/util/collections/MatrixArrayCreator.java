package core.foundation.util.collections;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static core.foundation.util.collections.ArrayCreator.createArrayWithSameDoubleNumber;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatrixArrayCreator {

    /**
     * Creates a diagonal matrix from a given array of diagonal values.
     *
     * @param diagonalValues the array of values to be placed on the diagonal
     * @return the resulting diagonal matrix
     */

    public static double[][] createDiagonalMatrix(double[] diagonalValues) {
        Preconditions.checkArgument(diagonalValues.length > 0, "Array must not be null or empty");
        int nDim=diagonalValues.length;
        double[][] matrix = new double[nDim][nDim];
        for (int i = 0; i < nDim; i++) {
            matrix[i][i] = diagonalValues[i];
        }
        return matrix;
    }

    /**
     * Creates a matrix filled with zeros.
     *
     * @param nDim the number of dimensions for the matrix (i.e., the number of rows and columns)
     * @return a square matrix with all elements set to zero
     */
    public static double[][] createAllZeroMatrix(int nDim) {
        return createDiagonalMatrix(createArrayWithSameDoubleNumber(nDim, 0.0));
    }

    /**
     * Creates a matrix filled with a specified value.
     *
     * @param nDim the number of dimensions for the matrix (i.e., the number of rows and columns)
     * @param value the value to fill the matrix with
     * @return a square matrix with all elements set to the specified value
     */
    public static double[][] createAllWithValueMatrix(int nDim, double value) {
        double[][] matrix = new double[nDim][nDim];
        for (int i = 0; i < nDim; i++) {
            for (int j = 0; j < nDim; j++) {
                matrix[i][j] = value;
            }
        }
        return matrix;
    }

}
