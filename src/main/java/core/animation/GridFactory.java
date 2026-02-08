package core.animation;

public class GridFactory {

    public static double[][] sincos(int rows, int cols) {
        return sincosRand(rows, cols,0.6,0.4);
    }

    public static double[][] sincosRand(int rows, int cols, double randSin, double randCos) {
        double[][] d = new double[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                d[r][c] = Math.sin(r * randSin) + Math.cos(c * randCos);
        return d;
    }

    public static double[][] sincosXyz(int i, int i1, double randSin, double randCos) {
       return toSeries(sincosRand(i, i1,randSin,randCos));
    }

    public static double[][] toSeries(double[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int n = rows * cols;
        double[] x = new double[n];
        double[] y = new double[n];
        double[] z = new double[n];

        int k = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                x[k] = c;
                y[k] = r;
                z[k] = grid[r][c];
                k++;
            }
        }
        return new double[][] { x, y, z }; // <-- längd 3
    }

}
