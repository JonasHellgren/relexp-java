package core.foundation.gadget.training;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * This class represents a collection of training data, specifically for errors.
 * Each example in the training data consists of an input and an error.
 * Error and output can't both be present at the same time.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TrainDataErr {

    TrainData trainData;

    public static TrainDataErr empty() {
        return new TrainDataErr(TrainData.empty());
    }

    public static TrainDataErr of(List<double[]> inputs, List<Double> errors) {
        return new TrainDataErr(TrainData.of(inputs, errors));
    }

    public List<List<Double>> inputsAsListList() {
        return trainData.inputsAsListList();
    }

    public void add(double[] input1, double error) {
        trainData.add(input1, error);
    }

    public void addListIn(List<Double> input, double error) {
        trainData.addListIn(input, error);
    }

    public void clear() {
        trainData.clear();
    }

    public TrainDataErr createBatch(int len) {
        return new TrainDataErr(trainData.createBatch(len));
    }

    public int nSamples() {
        return trainData.nSamples();
    }

    public boolean isEmpty() {
        return trainData.isEmpty();
    }

    public List<Double> inputAsList(int i) {
        return trainData.inputAsList(i);
    }

    public double[] input(int i) {
        return trainData.input(i);
    }

    //error instead of output
    public double error(int i) {
        return trainData.output(i);
    }

    @Override
    public String toString() {
        return trainData.toString();
    }


}
