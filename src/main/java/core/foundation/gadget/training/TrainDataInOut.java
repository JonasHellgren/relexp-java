package core.foundation.gadget.training;

import core.foundation.util.rand.RandUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of training data.
 * Each example in the training data consists of an input, an error OR an output.
 * Error and output can't both be present at the same time.
 */
public record TrainDataInOut(
        List<List<Double>> inputs,
        List<Double> outputs
) {


    public static TrainDataInOut empty() {
        return new TrainDataInOut(new ArrayList<>(), new ArrayList<>());
    }


    public static TrainDataInOut of(List<List<Double>> inputs, List<Double> outputs) {
        return new TrainDataInOut(inputs, outputs);
    }

    public void add(List<Double> input, double output) {
        inputs.add(input);
        outputs.add(output);
    }

    public void clear() {
        inputs.clear();
        outputs.clear();
    }

    public TrainDataInOut createBatch(int len) {
        var randomIndices = RandUtils.randomIndices(len, nSamples());
        var newInputs = new ArrayList<List<Double>>();
        var newOutputs = new ArrayList<Double>();
        for (int i : randomIndices) {
            newInputs.add(inputs.get(i));
            newOutputs.add(outputs.get(i));
        }
        return TrainDataInOut.of(newInputs, newOutputs);

    }

    public int nSamples() {
        return inputs.size();
    }

    public List<Double> input(int i) {
        return inputs.get(i);
    }

    public double output(int i) {
        return outputs.get(i);
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
