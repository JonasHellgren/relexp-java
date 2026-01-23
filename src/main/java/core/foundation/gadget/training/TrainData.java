package core.foundation.gadget.training;

import com.google.common.base.Preconditions;
import core.foundation.util.collections.Array2ListConverterUtil;
import core.foundation.util.collections.List2ArrayConverterUtil;
import core.foundation.util.rand.RandUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of training data.
 * Each example in the training data consists of an input, an error OR an output.
 * Error and output can't both be present at the same time.
 */
public record TrainData(
        List<double[]> inputs,
        List<Double> outputs
) {


    public static TrainData empty() {
        return new TrainData(new ArrayList<>(), new ArrayList<>());
    }

    public static TrainData ofListList(List<List<Double>> inputs, List<Double> outputs) {
        List<double[]> inputsArrList = new ArrayList<>();
        for (List<Double> input : inputs) {
            inputsArrList.add(List2ArrayConverterUtil.convertListToDoubleArr(input));
        }
        return new TrainData(inputsArrList, outputs);
    }

    public static TrainData of(List<double[]> inputs, List<Double> outputs) {
        return new TrainData(inputs, outputs);
    }

    public List<List<Double>> inputsAsListList() {
        List<List<Double>> inputsListList = new ArrayList<>();
        for (double[] input : inputs) {
            inputsListList.add(Array2ListConverterUtil.arrayToList(input));
        }
        return inputsListList;
    }

    public void add(double[] input1, double v) {
        inputs.add(input1);
        outputs.add(v);
    }

    public void addListIn(List<Double> input, double output) {
        inputs.add(List2ArrayConverterUtil.convertListToDoubleArr(input));
        outputs.add(output);
    }

    public void clear() {
        inputs.clear();
        outputs.clear();
    }

    public TrainData createBatch(int len) {
        var randomIndices = RandUtil.randomIndices(len, nSamples());
        var newInputs = new ArrayList<double[]>();
        var newOutputs = new ArrayList<Double>();
        for (int i : randomIndices) {
            newInputs.add(inputs.get(i));
            newOutputs.add(outputs.get(i));
        }
        return TrainData.of(newInputs, newOutputs);

    }

    public int nSamples() {
        return inputs.size();
    }


    public boolean isEmpty() {
        return nSamples() == 0;
    }

    public List<Double> inputAsList(int i) {
        validateIndex(i);
        return Array2ListConverterUtil.arrayToList(inputs.get(i));
    }

    public double[] input(int i) {
        validateIndex(i);
        return inputs.get(i);
    }

    public double output(int i) {
        return outputs.get(i);
    }


    private void validateIndex(int i) {
        Preconditions.checkArgument(i < nSamples() && i >= 0, "index out of bounds");
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
