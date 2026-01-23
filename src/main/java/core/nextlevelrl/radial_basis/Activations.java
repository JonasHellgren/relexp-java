package core.nextlevelrl.radial_basis;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the activations of a set of examples.
 * Each example has a list of activations corresponding to different kernels.
 */
@AllArgsConstructor
public class Activations {

    List<double[]> activationsAllExamples;  //  nExamples x nKernels
    int nDim;

    public static Activations of(int nExamples, int nDim) {
        return new Activations(createExampleList(nExamples, nDim), nDim);
    }

    public void clear() {
        activationsAllExamples.clear();
    }

    public void reset(int nExamples) {
        clear();
        activationsAllExamples=createExampleList(nExamples, nDim);
    }

    public static Activations empty(int nDim) {
        return new Activations(new ArrayList<>(), nDim);
    }


    private static List<double[]> createExampleList(int nExamples, int nDim) {
        List<double[]> list = new ArrayList<>();
        for (int i = 0; i < nExamples; i++) {
            list.add(new double[nDim]);
        }
        return list;
    }

    public void calculateActivations(TrainData data, Kernels kernels) {
        Preconditions.checkArgument(!isEmpty(), "activations should not be empty");
        for (int i = 0; i < data.inputs().size(); i++) {
            var input = data.inputs().get(i);
            var activationsOfInput = kernels.getActivationOfSingleInput(input);
            set(i, activationsOfInput);
        }
    }

    public boolean isEmpty() {
        return activationsAllExamples.isEmpty();
    }

    /**
     * Changes the activations for a specific example.
     *
     * @param idxSample   the index of the example
     * @param activations the new activations
     */

    public void set(int idxSample, double[] activations) {
        activationsAllExamples.set(idxSample, activations);
    }

    /**
     * Gets the activation for a specific example and kernel.
     *
     * @param idxSample the index of the sample
     * @param idxKernel the index of the kernel
     * @return the activation
     */
    public double get(int idxSample, int idxKernel) {
        return get(idxSample)[idxKernel];
    }

    public double[] get(int idxSample) {
        return activationsAllExamples.get(idxSample);
    }

    public int nSamples() {
        return activationsAllExamples.size();
    }

    public int nKernels() {
        Preconditions.checkArgument(nSamples() > 0, "activations should not be empty");
        return activationsAllExamples.get(0).length;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (double[] activation : activationsAllExamples) {
            int idx = activationsAllExamples.indexOf(activation);
            sb.append("  Sample idx:=").append(idx).append(":").append(activation).append("\n");
        }
        return "\n" + sb;
    }


}
