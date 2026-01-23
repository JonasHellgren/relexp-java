package chapters.ch9.gradient_descent;

import com.google.common.base.Preconditions;
import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.Weights;
import core.foundation.util.collections.ListUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * A linear fitter class that uses gradient descent to fit a linear model to the training data.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LinearFitter {
    private final TrainData data;
    private final WeightUpdaterLinear updater;
    private final Weights weights;
    private final OutPutCalculator calculator;

    public static LinearFitter of(TrainData data, PhiExtractor phiExtractor, double learningRate) {
        return new LinearFitter(
                data,
                WeightUpdaterLinear.of(learningRate, phiExtractor),
                Weights.allZero(phiExtractor.nPhis()),
                OutPutCalculator.of(phiExtractor));
    }

    /**
     * Fits the linear model to the training data using gradient descent.
     *
     * @param nEpochs the number of epochs to train for
     * @param batchSize the batch size used to update the weights
     */
    public void fit(int nEpochs, int batchSize) {
        validateFit(data, nEpochs, batchSize);
        for (int epoch = 0; epoch < nEpochs; epoch++) {
            updater.updateWeights(data.createBatch(batchSize), weights);
        }
    }

    public List<Double> fitAndReturnErrorPerEpoch(int nEpochs, int batchSize) {
        validateFit(data, nEpochs, batchSize);
        var errors = new ArrayList<Double>();
        var outRef=data.outputs();
        for (int epoch = 0; epoch < nEpochs; epoch++) {
            errors.add(getErrAvg(outRef));
            updater.updateWeights(data.createBatch(batchSize), weights);
        }
        return errors;
    }

    /**
     * Calculates the outputs for a list of inputs using the linear model.
     *
     * @param inputs a list of input vectors
     * @return a list of calculated outputs
     */
    public List<Double> calcOutputs(List<List<Double>> inputs) {
        return inputs.stream().map(this::calcOut).toList();
    }

    /**
     * Calculates the output of the linear model for a given input.
     *
     * @param input the input data
     * @return the calculated output
     */
    public double calcOut(List<Double> input) {
        return calculator.outPut(weights, input);
    }


    private double getErrAvg(List<Double> outRef) {
        var out = calcOutputs(data.inputsAsListList());
        var diffList=(ListUtil.elementSubtraction(out, outRef));
        var diffAbs= ListUtil.everyItemAbsolute(diffList);
        return ListUtil.findAverage(diffAbs).orElseThrow();
    }

    private static void validateFit(TrainData data, int nEpochs, int batchSize) {
        Preconditions.checkArgument(nEpochs > 0, "nEpochs should be > 0");
        Preconditions.checkArgument(data.nSamples() >= batchSize, "nSamples should be >= batchSize");
    }

    @Override
    public String toString() {
        return "LinearFitter{" +
                ", weights=" + weights +
                '}';
    }

}
