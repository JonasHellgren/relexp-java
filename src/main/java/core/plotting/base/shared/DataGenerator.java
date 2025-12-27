package core.plotting.base.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {


    /**
     * Generates a set of random walk data, where each point is the previous point plus a random value between -0.5 and 0.5.
     *
     * @param numPoints	The number of points to generate in the random walk.
     * @return         	An array of doubles representing the random walk data.
     */
    public static double[] getRandomWalk(int numPoints) {

        double[] y = new double[numPoints];
        y[0] = 0;
        for (int i = 1; i < y.length; i++) {
            y[i] = y[i - 1] + Math.random() - .5;
        }
        return y;
    }

    /**
     * Generates a list of random double values following a Gaussian distribution.
     *
     * @param count  the number of random values to generate
     * @return      a list of random double values
     */
    public static List<Double> getGaussianData(int count) {

        List<Double> data = new ArrayList<>(count);
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            data.add(r.nextGaussian() * 10);
        }
        return data;
    }

}
