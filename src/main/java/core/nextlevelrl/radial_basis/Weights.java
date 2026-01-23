package core.nextlevelrl.radial_basis;

import com.google.common.base.Preconditions;
import core.foundation.util.collections.List2ArrayConverterUtil;
import lombok.AllArgsConstructor;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleSupplier;

@AllArgsConstructor
public class Weights {

    private double[] weights;

    public static Weights allZero(int size) {
       return new Weights(createWeightsAllZero(size));
    }

    public double[] getWeights() {
        double[] weightsCop= new double[size()];
        for (int i = 0; i < size(); i++) {
            weightsCop[i]=get(i);
        }
        return weightsCop;
    }

    public static Weights allWithValue(int size, DoubleSupplier valueSupplier) {
        var weights= new Weights(createWeightsAllZero(size));
        for (int i = 0; i < size; i++) {
            weights.set(i, valueSupplier.getAsDouble());
        }
        return weights;
    }

    public static Weights of(List<Double> list) {
        return new Weights(List2ArrayConverterUtil.convertListToDoubleArr(list));
    }

    public double get(int index) {
        Preconditions.checkArgument(index >= 0 && index < weights.length,
                "Non valid weight index");
        return weights[index];
    }

    public int size() {
        return weights.length;
    }

    public void setWeights(double[] doubles) {
        Preconditions.checkArgument(doubles.length == size(),
                "weights and doubles should have same length");
        for (int i = 0; i <size() ; i++) {
            set(i,doubles[i]);
        }
    }

    public void set(int i, double v) {
        weights[i] = v;
    }

    private static double[] createWeightsAllZero(int size) {
        return new double[size];
    }


    @Override
    public String toString() {
        return "Weights{" +
                "weights=" + Arrays.toString(weights) +
                '}';
    }

}
