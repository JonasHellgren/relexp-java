package core.nextlevelrl.radial_basis;

import com.google.common.base.Preconditions;
import core.foundation.gadget.math.Accumulator;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.collections.List2ArrayConverterUtil;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.stream.IntStream;


@AllArgsConstructor
@Getter
public class RbfNetwork {
    Kernels kernels;
    Activations activations;
    Weights weights;
    WeightUpdater updater;
    Accumulator lossCalculator;
    TdErrorClipper clipper;

    public static RbfNetwork of(Kernels kernels, double learningRate, int nDim) {
        return of(kernels,learningRate,()->0, Double.MAX_VALUE,nDim);
    }

    public static RbfNetwork of(Kernels kernels,
                                double learningRate,
                                DoubleSupplier valueSupplier,
                                double errorBound,
                                int nDim) {
        return new RbfNetwork(kernels,
                Activations.empty(nDim),
                Weights.allWithValue(kernels.size(),valueSupplier),
                WeightUpdater.of(learningRate),
                Accumulator.empty(),
                TdErrorClipper.of(errorBound));
    }

    public int nKernels() {
        return kernels.size();
    }

    public double loss() {
        return lossCalculator.value()/kernels.size();
    }

    public void setWeights(double[] w) {
        weights.setWeights(w);
    }

    public void setLearningRate(double learningRate) {
        updater.setLearningRate(learningRate);
    }

    /**
     * Fits the RBF network to the training data.
     *
     * @param data the training data to fit the network to
     */

    public void fit(TrainData data, int nFits) {
        lossCalculator.reset();
        IntStream.range(0, nFits).forEach(i -> fit(data));
    }

    public int nClips() {
        return clipper.nClips();
    }


    public int errorListSize() {
        return clipper.errorListSize();
    }

    /**
     * Computes the output of the RBF network for a given input.
     *
     * @param input the input data
     * @return the output of the RBF network
     */
    public double outPut(double[] input) {
        return calcOutput(getActivation(input), weights);
    }


    public double outPutListIn(List<Double> in) {
        return calcOutput(getActivation(List2ArrayConverterUtil.convertListToDoubleArr(in)), weights);
    }

    public double[] getActivation(double[] input) {
        return kernels.getActivationOfSingleInput(input);
    }

/*
    public Double outFromActivation(double[] activation, ActionPark a) {
        return calcOutput(activation, weights);
    }
*/

    public void copyWeights(RbfNetwork rbfNetwork) {
        weights.setWeights(rbfNetwork.weights.getWeights());
    }

    private void fit(TrainData data) {
        var errors = getErrors(data.inputs(), data.outputs());
        var errorsClipped=clipper.clip(errors);
        lossCalculator.add(errorsClipped.stream().mapToDouble(Math::abs).sum());
        fitFromErrors(TrainData.of(data.inputs(), errorsClipped));
    }


    /**
     * Updates the weights of the RBF network based on the input data and error values.
     */
    private void fitFromErrors(TrainData data) {
        int nInputs = data.inputs().size();
        Preconditions.checkArgument(!data.isEmpty(), "data must not be empty");
        ConditionalsUtil.executeIfTrue(nInputs != activations.nSamples(), () -> activations.reset(nInputs));
        activations.calculateActivations(data, kernels);
        updater.updateWeights(data, activations, weights);
    }

    private List<Double> getErrors(List<double[]> inputs, List<Double> yTargets) {
        return IntStream.range(0, inputs.size())
                .mapToDouble(i -> yTargets.get(i) - outPut(inputs.get(i)))
                .boxed().toList();
    }

    private double calcOutput(double[] activation, Weights weights) {
        double result = 0;
        for (int i = 0; i < weights.size(); i++) {
            result += weights.get(i) * activation[i];
        }
        return result;
    }


    @Override
    public String toString() {
        return "RbfNetwork{" +
                "kernels=" + kernels +
                ", activations=" + activations +
                ", weights=" + weights +
                '}';
    }

}
