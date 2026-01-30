package core.nextlevelrl.radial_basis;

import com.google.common.base.Preconditions;
import core.foundation.gadget.math.Accumulator;
import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.TrainDataErr;
import core.foundation.gadget.training.TrainDataOld;
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

    public void fitFromErrors(TrainDataErr data, int nFits) {
        lossCalculator.reset();
        IntStream.range(0, nFits).forEach(i -> fitFromErrors(data, true));
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

    public void copyWeights(RbfNetwork rbfNetwork) {
        weights.setWeights(rbfNetwork.weights.getWeights());
    }

    private void fit(TrainData data) {
        var errors = getErrors(data.inputs(), data.outputs());
        var errorsClipped=clipper.clip(errors);
        lossCalculator.add(errorsClipped.stream().mapToDouble(Math::abs).sum());
        fitFromErrors(TrainDataErr.of(data.inputs(), errorsClipped), true);
    }

    /**
     * Handy to save computation time if you already have the activations in other identical rbf
     *
     * @param other, the other rbf to copy from
     */
    public void fitUsingActivationsOtherRbf(TrainData data, int nEpochs,  RbfNetwork other) {
        copyActivations(other);
        fitWithUpdateActivationFlag(data, nEpochs, false);
    }

    public void copyActivations(RbfNetwork other) {
        //validateOtherRbf(other, nKernels());
        var activationsOther = other.activations;
        activations = RbfNetworkHelper.createIfNotEqualNofSamples(activationsOther.nSamples(), activations);
        RbfNetworkHelper.copyActivations(activationsOther, activations);
    }

    private void fitWithUpdateActivationFlag(TrainData data0, int nEpochs,  boolean updateActivations) {
      //  validate(data, nFits, batchSize);
        var errors = getErrors(data0.inputs(), data0.outputs());
        var data=TrainDataErr.of(data0.inputs(), errors);
        IntStream.range(0, nEpochs).forEach(i -> fitFromErrors(data, updateActivations));
    }


    /**
     * Updates the weights of the RBF network based on the input data and error values.
     */
    private void fitFromErrors(TrainDataErr data, boolean updateActivation) {
        int nInputs = data.nSamples();
        Preconditions.checkArgument(!data.isEmpty(), "data must not be empty");
        ConditionalsUtil.executeIfTrue(nInputs != activations.nSamples(), () -> activations.reset(nInputs));

        ConditionalsUtil.executeIfTrue(updateActivation, () -> {
            activations = RbfNetworkHelper.createIfNotEqualNofSamples(data.nSamples(), activations);
            activations.calculateActivations(data, kernels);
        });


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
