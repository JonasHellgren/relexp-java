package core.animation;

import org.jfree.data.xy.DefaultXYZDataset;

public final class GridToXYZDataset {
    private GridToXYZDataset() {}

    public static DefaultXYZDataset from(double[][] grid, String heat) {
        int rows = grid.length;
        int cols = grid[0].length;

        double[] x = new double[rows * cols];
        double[] y = new double[rows * cols];
        double[] z = new double[rows * cols];

        int k = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                x[k] = c;         // kolumnindex
                y[k] = r;         // radindex
                z[k] = grid[r][c];
                k++;
            }
        }

        DefaultXYZDataset ds = new DefaultXYZDataset();
        ds.addSeries(heat, new double[][]{x, y, z});
        return ds;
    }
}
