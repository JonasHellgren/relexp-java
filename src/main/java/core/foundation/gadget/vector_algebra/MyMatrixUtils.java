package core.foundation.gadget.vector_algebra;

import lombok.Builder;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;

public class MyMatrixUtils {

    @Builder
    public record Properties(int nRows, int nColumns) {
    }





    /**
     * Creates a zero vector of a specified size.
     *
     * @param size the size of the vector to be created
     * @return a RealVector with all elements set to zero
     */
    public static RealVector createZeroVector(int size) {
        return MatrixUtils.createRealVector(new double[size]);
    }

    /**
     * Creates a one vector of a specified size.
     *
     * @param size the size of the vector to be created
     * @return a RealVector with all elements set to zero
     */
    public static RealVector createOnesVector(int size) {
        double[] ones = new double[size];
        Arrays.fill(ones, 1.0);
        return MatrixUtils.createRealVector(ones);
    }

    /**
     * Creates a diagonal matrix from the given diagonal values.
     *
     * A diagonal matrix is a square matrix where all elements outside the main diagonal are zero.
     *
     * @param diagonal the diagonal values of the matrix to be created
     * @return a RealMatrix with the given diagonal values
     */
    public static RealMatrix createDiagonalMatrix(double[] diagonal) {
        int size = diagonal.length;
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++) {
            data[i][i] = diagonal[i];
        }
        return MatrixUtils.createRealMatrix(data);
    }

    /**
     * Creates an identity matrix of a specified size.
     *
     * An identity matrix is a square matrix where all elements of the principal diagonal are ones,
     * and all other elements are zeros.
     *
     * @param size the size of the identity matrix to be created
     * @return a RealMatrix representing the identity matrix of the specified size
     */
    public static RealMatrix identity(int size) {
        return MatrixUtils.createRealIdentityMatrix(size);
    }

    /**
     * Returns the properties of a given matrix.
     *
     * This method creates a new MatrixProperties object, which contains the number of rows and columns in the matrix.
     *
     * @param a the input matrix
     * @return a MatrixProperties object containing the dimensions of the matrix
     */
    public static Properties properties(RealMatrix a) {
        return new Properties(a.getRowDimension(), a.getColumnDimension());
    }


    public static Properties properties(RealVector a) {
        return new Properties(a.getDimension(), 1);
    }


}
