package chapters.ch9.radial_basis_old;

import com.google.common.base.Preconditions;
import core.foundation.gadget.training.Weights;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class RbfNetworkHelper {

    public static double calcOutput(List<Double> activation, Weights weights) {
        double result = 0;
        for (int i = 0; i < weights.size(); i++) {
            result += weights.get(i) * activation.get(i);
        }
        return result;
    }

    public static void copyActivations(Activations activationsOther, Activations activations) {
        Preconditions.checkArgument(activations.nSamples() == activationsOther.nSamples(),
                "Nof samples should be the same");
        for (int i = 0; i < activationsOther.nSamples(); i++) {
            activations.set(i, activationsOther.get(i));
        }
    }


    public static Activations createIfNotEqualNofSamples(int x, Activations activations) {
        return (activations.nSamples() != x)
                ? Activations.of(x)
                : activations;
    }

    public static void validateInput(List<Double> input, Weights weights, Kernels kernels) {
        int nKernels = kernels.size();
        Preconditions.checkArgument(nKernels > 0, "kernels should not be empty");
        Preconditions.checkArgument(nKernels == weights.size(),
                "kernels size should be same as weights length");
        int lengthCenterCoord = kernels.get(0).centerCoordinates().length;
        Preconditions.checkArgument(lengthCenterCoord == input.size(),
                "input size should be same as n dimension in any kernel, input size: " + input.size() +
                        ", lengthCenterCoord = " + lengthCenterCoord);
    }


    public static void validateOtherRbf(RbfNetwork other, int nKernels) {
        Preconditions.checkArgument(other.nKernels() == nKernels
                , "kernels should be same size");
    }


}
