package core.nextlevelrl.radial_basis;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.stream.IntStream;

/**
 * This class is responsible for updating the weights of a Radial Basis Function (RBF) network.
 * It provides methods for updating the weights based on the input data and target outputs.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
public class WeightUpdater {
    double learningRate;

    public static WeightUpdater of(double learningRate) {
        return new WeightUpdater(learningRate);
    }

    /**
     * Updates the weights based on the errors and activations.
     *
     * @param data        The training data.
     * @param weights     The current weights.
     * @param activations The activations of the network.
     */
    public void updateWeights(TrainData data, Activations activations, Weights weights) {
        validate(data, weights, activations);
        double[] gradient = weightGradientFromErrors(data, activations);
        for (int i = 0; i < weights.size(); i++) {
            double wOld = weights.get(i);
            weights.set(i, wOld + learningRate * gradient[i]);
        }
    }

    /***
     * The function computes the gradient of weights by aggregating the weighted errors
     * (error × activation) for all input samples for each kernel.
     *
     * gradient[idxKernel] = (1/nExamples) * ∑yErr[i] * φ(x[i], idxKernel)
     */
    private double[] weightGradientFromErrors(TrainData data, Activations activations) {
        int nKernels = activations.nKernels();
        return IntStream.range(0, nKernels)
                .mapToDouble(idxKernel -> getElementInGradient(data, activations, idxKernel))
                .toArray();

    }

    private static double getElementInGradient(TrainData data,
                                               Activations activations,
                                               int idxKernel) {
        int nExamples = data.nSamples();
        double elementInGradient = 0;
        for (int idxExample = 0; idxExample < nExamples; idxExample++) {
            double error = data.output(idxExample);
            double activation = activations.get(idxExample, idxKernel);
            elementInGradient += error * activation;
        }
        elementInGradient = elementInGradient / nExamples;
        return elementInGradient;
    }


    private static void validate(TrainData data, Weights weights, Activations activations) {
        Preconditions.checkArgument(weights.size() == activations.nKernels(),
                "weights and nKernels should have same length, nWeights = " + weights.size()
                        + ", nKernels = " + activations.nKernels());
        Preconditions.checkArgument(data.nSamples() == activations.nSamples()
                , "data and activations should have same length");
    }


}
