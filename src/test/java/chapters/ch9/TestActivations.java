package chapters.ch9;

import core.foundation.gadget.training.TrainDataErr;
import core.nextlevelrl.radial_basis.Activations;
import core.nextlevelrl.radial_basis.Kernel;
import core.nextlevelrl.radial_basis.Kernels;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

 class TestActivations {


    public static final int N_DIM = 2;

    @Test
     void testSize() {
        Activations activations = Activations.of(5, N_DIM);
        assertEquals(5, activations.nSamples());
    }

    @Test
     void testOf() {
        int nExamples = 5;
        Activations activations = Activations.of(nExamples, N_DIM);
        assertEquals(nExamples, activations.nSamples());
        for (int i = 0; i < nExamples; i++) {
            assertTrue(activations.activationsAllExamples.get(i).length == N_DIM);
        }
    }

    @Test
     void testEmpty() {
        Activations activations = Activations.empty(N_DIM);
        assertEquals(0, activations.nSamples());
    }


     @Test
     void testChangeActivations() {
         TrainDataErr data = TrainDataErr.of(
                 List.of(new double[]{1.0}, new double[]{3.0}),
                 List.of(1.0, 1.0));
         Kernels kernels = Kernels.empty();
         Kernel kernel = Kernel.ofSigmas(new double[]{1.0}, new double[]{1.0});
         kernels.addKernel(kernel);

         Activations activations = Activations.of(data.nSamples(), N_DIM);
         activations.calculateActivations(data, kernels);

         // Verify that activations were changed
         assertEquals(N_DIM, activations.nSamples());
         assertNotNull(activations.activationsAllExamples.get(0));
         assertNotNull(activations.activationsAllExamples.get(1));

     }


}
