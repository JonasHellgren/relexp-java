package chapters.ch9.radial_basis_old;

import com.google.common.base.Preconditions;
import core.foundation.gadget.training.TrainDataOld;
import core.foundation.gadget.training.Weights;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import java.util.stream.IntStream;
import static chapters.ch9.radial_basis_old.RbfNetworkHelper.*;


@AllArgsConstructor
@Getter
public class RbfNetwork {
    Kernels kernels;
    Activations activations;
    Weights weights;
    WeightUpdaterRbf updater;

    public static RbfNetwork of(Kernels kernels, double learningRate) {
        return new RbfNetwork(kernels,
                Activations.empty(),
                Weights.allZero(kernels.size()),
                WeightUpdaterRbf.of(learningRate));
    }

    public int nKernels() {
        return kernels.size();
    }

    public void setWeights(double[] doubles) {
        weights.setWeights(doubles);
    }


    /**
     * Fits the RBF network to the training data.
     *
     * @param data      the training data to fit the network to
     * @param nEpochs   the number of epochs to train for
     * @param batchSize the batch size used to update the weights
     */
    public void fit(TrainDataOld data, int nEpochs, int batchSize) {
        fitWithUpdateActivationFlag(data, nEpochs, batchSize, true);
    }

    /**
     * Handy to save computation time if you already have the activations in other identical rbf
     *
     * @param other, the other rbf to copy from
     */
    public void fitUsingActivationsOtherRbf(TrainDataOld data, int nEpochs, int batchSize, RbfNetwork other) {
        copyActivations(other);
        fitWithUpdateActivationFlag(data, nEpochs, batchSize, false);
    }

    /**
     * Computes the output of the RBF network for a given input.
     *
     * @param input the input data
     * @return the output of the RBF network
     */
    public double outPut(List<Double> input) {
        validateInput(input, weights, kernels);
        return calcOutput(kernels.getActivationOfSingleInput(input), weights);
    }


    public void copyActivations(RbfNetwork other) {
        validateOtherRbf(other, nKernels());
        var activationsOther = other.activations;
        activations = createIfNotEqualNofSamples(activationsOther.nSamples(), activations);
        RbfNetworkHelper.copyActivations(activationsOther, activations);
    }

    private void fitWithUpdateActivationFlag(TrainDataOld data, int nEpochs, int batchSize, boolean updateActivations) {
        validate(data, nEpochs, batchSize);
        IntStream.range(0, nEpochs).forEach(i -> fitMayUpdateActivations(data, updateActivations));
    }

    /**
     * TODO: errors should be calculated in other method, now entire data is evaluated, even if small batchSize
     */
    private void fitMayUpdateActivations(TrainDataOld data, boolean updateActivations) {
        var errors = (data.isErrors() ? data.errors() : getErrors(data.inputs(), data.outputs()));
        fitFromErrors(data.inputs(), errors, updateActivations);
    }

    private static void validate(TrainDataOld data, int nEpochs, int batchSize) {
        Preconditions.checkArgument(nEpochs > 0, "nFits should be > 0");
        Preconditions.checkArgument(data.nSamples() >= batchSize, "nSamples should be >= batchSize");
    }

    /**
     * Updates the weights of the RBF network based on the input data and error values.
     *
     * @param inputs the input data
     * @param errors the error values
     */
    private void fitFromErrors(List<List<Double>> inputs, List<Double> errors, boolean updateActivations) {
        var data = TrainDataOld.ofErrors(inputs, errors);
        ConditionalsUtil.executeIfTrue(updateActivations, () -> {
            activations = createIfNotEqualNofSamples(data.nSamples(), activations);
            activations.calculateActivations(data, kernels);
        });
        updater.updateWeights(data, activations, weights);
    }

    private List<Double> getErrors(List<List<Double>> inputs, List<Double> yTargets) {
        return IntStream.range(0, inputs.size())
                .mapToDouble(i -> yTargets.get(i) - outPut(inputs.get(i)))
                .boxed().toList();
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
