package core.foundation.util.math;

import core.foundation.util.list.Array2ListConverter;
import core.foundation.util.list.List2ArrayConverter;
import lombok.experimental.UtilityClass;
import java.util.List;

@UtilityClass
public class SoftMax {

    public static List<Double> softmax(List<Double> input) {
        return Array2ListConverter.arrayToList(
                softmax(List2ArrayConverter.convertListToDoubleArr(input)));
    }

    public static double[] softmax(double[] input) {
        double max = Double.NEGATIVE_INFINITY;
        for (double x : input) {
            max = Math.max(max, x);
        }

        double sum = 0;
        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = Math.exp(input[i] - max);
            sum += output[i];
        }

        for (int i = 0; i < input.length; i++) {
            output[i] /= sum;
        }

        return output;
    }

}
