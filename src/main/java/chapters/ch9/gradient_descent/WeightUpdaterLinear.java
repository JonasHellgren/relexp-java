package chapters.ch9.gradient_descent;

import com.google.common.base.Preconditions;
import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.TrainDataErr;
import core.foundation.gadget.training.Weights;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * This class is responsible for updating the weights of a linear model using gradient descent.
 * It provides methods for updating the weights based on the input data and target outputs.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeightUpdaterLinear {

    double learningRate;
    PhiExtractor phiExtractor;
    OutPutCalculator calculator;

    public static WeightUpdaterLinear of(double learningRate, PhiExtractor phiExtractor) {
        return new WeightUpdaterLinear(learningRate,phiExtractor,OutPutCalculator.of(phiExtractor));
    }

    /**
     * Updates the weights based on the errors and phis.
     *
     * @param data0        The training data.
     * @param weights     The current weights.
     */

    public void updateWeights(TrainData data0, Weights weights) {
        Preconditions.checkArgument(weights.size() == phiExtractor.nPhis(),
                "weights and nPhis should have same length, nWeights = " + weights.size() +
                        ", nPhis = " + phiExtractor.nPhis());

        var data=trainDataWithErrors(data0, data0.outputs(), weights);

        double[] gradient = weightGradientFromErrors(data);
        for (int i = 0; i < weights.size(); i++) {
            double wOld = weights.get(i);
            weights.set(i, wOld + learningRate * gradient[i]);
        }
    }

    /***
     * The function computes the gradient of weights by aggregating the weighted errors
     * (error × activation) for all input samples for each dimension.
     *
     * gradient[idxDimension] = (1/nExamples) * ∑yErr[i] * φ(x[i], idxDimension)
     */

    private double[] weightGradientFromErrors(TrainDataErr data) {
        int nExamples = data.nSamples();
        int nPhis = phiExtractor.nPhis();
        double[] gradient = new double[nPhis];
        for (int idxDimension = 0; idxDimension < nPhis; idxDimension++) {
            for (int idxExample = 0; idxExample < nExamples; idxExample++) {
                double phi = phiExtractor.getPhi(data.inputAsList(idxExample), idxDimension);
                double error = data.error(idxExample);
                gradient[idxDimension] += error * phi;
            }
            gradient[idxDimension] = gradient[idxDimension] / nExamples;
        }
        return gradient;
    }

    private TrainDataErr trainDataWithErrors(TrainData data, List<Double> yTargets, Weights weights) {
        var newData = TrainDataErr.empty();
        for (int i = 0; i < data.nSamples(); i++) {
            double error = yTargets.get(i) - calculator.outPut(weights,data.inputAsList(i));
            newData.add(data.input(i), error);
        }
        return newData;
    }



}
