package chapters.ch9;

import core.nextlevelrl.radial_basis.Kernel;
import core.nextlevelrl.radial_basis.Kernels;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestKernels {

    @Test
    void testEmpty() {
        Kernels kernels = Kernels.empty();
        assertEquals(0, kernels.size());
    }

    @Test
    void testAddKernel() {
        Kernels kernels = Kernels.empty();
        Kernel kernel = Kernel.ofSigmas(new double[]{1.0}, new double[]{1.0});
        kernels.addKernel(kernel);
        assertEquals(1, kernels.size());
        assertEquals(kernel, kernels.get(0));
    }

    @Test
    void testAddKernelsWithCentersAndSigmas() {
        Kernels kernels = Kernels.empty();
        double[] centers = new double[]{1.0, 2.0};
        double[] sigmas = new double[]{1.0, 2.0};
        kernels.addKernelsWithCentersAndSigmas(centers, sigmas);
        assertEquals(2, kernels.size());
    }

    @Test
    void testGet() {
        Kernels kernels = Kernels.empty();
        Kernel kernel1 = Kernel.ofSigmas(new double[]{1.0}, new double[]{1.0});
        Kernel kernel2 = Kernel.ofSigmas(new double[]{2.0}, new double[]{2.0});
        kernels.addKernel(kernel1);
        kernels.addKernel(kernel2);
        assertEquals(kernel1, kernels.get(0));
        assertEquals(kernel2, kernels.get(1));
    }

    @Test
    void testGetActivationOfSingleInput() {
        Kernels kernels = Kernels.empty();
        Kernel kernel1 = Kernel.ofSigmas(new double[]{1.0}, new double[]{1.0});
        Kernel kernel2 = Kernel.ofSigmas(new double[]{2.0}, new double[]{2.0});
        kernels.addKernel(kernel1);
        kernels.addKernel(kernel2);
        //List<Double> input = List.of(1.0);
        double[] input = new double[]{1.0};
        var activations = kernels.getActivationOfSingleInputNoNormalize(input);
        System.out.println("activations = " + activations);
        assertTrue(activations[0]>activations[1]);  //input closer to kernel 0
        assertEquals(kernel1.activation(new double[]{1.0}), activations[0]);
    }

}
