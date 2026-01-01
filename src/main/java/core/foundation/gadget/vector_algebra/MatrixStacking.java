package core.foundation.gadget.vector_algebra;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

public class MatrixStacking {

    private MatrixStacking() {
    }

    /**
     * This method stacks a list of RealMatrix objects vertically.
     * The resulting matrix will have the same number of columns as the matrices in the list,
     * and the total number of rows will be the sum of the rows of all the matrices.
     *
     * @param matrices The list of RealMatrix objects to stack vertically.
     * @return A RealMatrix object representing the vertically stacked matrices.
     * @throws IllegalArgumentException if the list of matrices is empty,
     * or if the matrices have different numbers of columns.
     */
    public static RealMatrix stackVertically(List<RealMatrix> matrices) {
        // Check if the list is empty
        if (matrices.isEmpty()) {
            throw new IllegalArgumentException("The list of matrices is empty.");
        }

        // Get the number of columns from the first matrix
        int cols = matrices.get(0).getColumnDimension();

        // Calculate the total number of rows by summing the rows of each matrix
        int totalRows = 0;
        for (RealMatrix matrix : matrices) {
            if (matrix.getColumnDimension() != cols) {
                throw new IllegalArgumentException("All matrices must have the same number of columns.");
            }
            totalRows += matrix.getRowDimension();
        }

        // Create a new matrix with total rows and same number of columns
        RealMatrix stackedMatrix = MatrixUtils.createRealMatrix(totalRows, cols);

        // Copy the rows of each matrix into the new stacked matrix
        int currentRow = 0;
        for (RealMatrix matrix : matrices) {
            int rows = matrix.getRowDimension();
            for (int i = 0; i < rows; i++) {
                stackedMatrix.setRow(currentRow, matrix.getRow(i));
                currentRow++;
            }
        }

        return stackedMatrix;
    }

    /**
     * Stacks a list of RealVector objects horizontally into a single RealVector.
     *
     * The resulting vector will have a dimension equal to the sum of the dimensions of the input vectors.
     *
     * @param vectors the list of RealVector objects to stack horizontally
     * @return a RealVector representing the horizontally stacked vectors
     */

    public static RealVector stackVectorsHorizontally(List<RealVector> vectors) {
        int numElements = vectors.get(0).getDimension();
        int numVectors = vectors.size();
        RealMatrix matrix = new Array2DRowRealMatrix(1, numElements * numVectors);

        int index = 0;
        for (RealVector vector : vectors) {
            for (int i = 0; i < numElements; i++) {
                matrix.setEntry(0, index++, vector.getEntry(i));
            }
        }

        return matrix.getRowVector(0);
    }


    /**
     * Converts a list of RealVector objects into a RealMatrix.
     *
     * The resulting matrix will have the same number of rows as the dimension of the vectors,
     * and the same number of columns as the number of vectors in the list.
     *
     * @param vectors the list of RealVector objects to convert
     * @return a RealMatrix representing the vectors
     */

    public static RealMatrix vectorsToMatrix(List<RealVector> vectors) {
        int numRows = vectors.get(0).getDimension();
        int numCols = vectors.size();
        double[][] data = new double[numRows][numCols];
        for (int i = 0; i < numCols; i++) {
            RealVector vector = vectors.get(i);
            for (int j = 0; j < numRows; j++) {
                data[j][i] = vector.getEntry(j);
            }
        }
        return new Array2DRowRealMatrix(data);
    }


}
