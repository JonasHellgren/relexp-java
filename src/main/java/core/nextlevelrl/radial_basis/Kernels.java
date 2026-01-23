package core.nextlevelrl.radial_basis;

import com.google.common.base.Preconditions;
import core.foundation.util.collections.ArrayUtil;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class represents a collection of Kernel objects.
 * Each Kernel represents a radial basis function with a specific center and gamma value.
 */
@AllArgsConstructor
@Log
public class Kernels {

    public static final double EPS = 1e-6;
    List<Kernel> kernels;

    public static Kernels empty() {
        return new Kernels(new ArrayList<>());
    }

    public int size() {
        return kernels.size();
    }

    public void addKernel(Kernel kernel) {
        kernels.add(kernel);
    }

    public void addKernelsWithCentersAndSigmas(double[] centers, double[] sigmas) {
        for (int i = 0; i < centers.length; i++) {
            addKernel(Kernel.ofSigmas(new double[]{centers[i]}, new double[]{sigmas[i]}));
        }
    }

    public Kernel get(int index) {
        Preconditions.checkArgument(index >= 0 && index < nKernels(),
                "kernel index should be between 0 and " + (nKernels() - 1));
        return kernels.get(index);
    }

    private int nKernels() {
        return kernels.size();
    }

    public double[]  getActivationOfSingleInput(double[] input) {
        Preconditions.checkArgument(size()>0
                , "kernels should not be empty");
        Preconditions.checkArgument(input.length == kernels.get(0).nDimensions()
                ,"input size should be same as kernels dimensions");
        double[] activations = new double[nKernels()];
        int i=0;
        for (Kernel kernel : kernels) {
            activations[i]=kernel.activation(input);
            i++;
        }
        double sum = ArrayUtil.sum(activations);
        maybeLog(input, sum);
       // return activations;
        return normalize(activations,sum);
    }

    private static void maybeLog(double[] input, double sum) {
        ConditionalsUtil.executeIfTrue(sum < EPS,() -> log.info("sum of activations are smaller than EPS: " +
                sum +", input: " + Arrays.toString(input)));
    }


    /**
     * Normalization ensures that RBF outputs remain stable and unbiased even when fewer kernels are active,
     * such as near state-space boundaries or operational limits.
     */

    private static double[] normalize(double[] activations, double sum) {
        double scaler = 1.0 / sum;
        return sum < EPS
                ? ArrayUtil.multiplyWithValue(activations, 1.0 / (sum+EPS))
                : ArrayUtil.multiplyWithValue(activations, scaler);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (Kernel kernel : kernels) {
            sb.append(kernel).append("\n");
        }
        return "\n" + sb;
    }

}
