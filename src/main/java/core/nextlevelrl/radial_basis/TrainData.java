package core.nextlevelrl.radial_basis;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of training data.
 * Each example in the training data consists of an input, and  an output.
 * Error and output can't both be present at the same time.
 * double[] used in inputs to speed up calculations
 */

public record TrainData(
        List<double[]> inputs,
        List<Double> outputs
) {


    public static TrainData empty() {
        return new TrainData(new ArrayList<>(),  new ArrayList<>());
    }

    public static TrainData of(List<double[]> inputs, List<Double> outputs) {
        return new TrainData(inputs, outputs);
    }

     public void addIAndOut(double[] input, double output) {
        inputs.add(input);
        outputs.add(output);
    }

    public void clear() {
        inputs.clear();
        outputs.clear();
    }


    public boolean isEmpty() {
        return inputs.isEmpty();
    }

    public int nSamples() {
        return inputs.size();
    }

    public double[] input(int i) {
        checkIndex(i);
        return inputs.get(i);
    }

    public double output(int i) {
        checkIndex(i);
        return outputs.get(i);
    }


    private void checkIndex(int i) {
        Preconditions.checkArgument(i < nSamples(), "i should be smaller than nSamples");
    }


    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int i = 0; i < nSamples(); i++) {
                sb.append(input(i)).append(" ").append(output(i)).append("\n");
        }
        return sb.toString();
    }

}
