package core.radial_basis;

import core.foundation.gadget.training.TrainData;
import core.nextlevelrl.radial_basis.Kernel;
import core.nextlevelrl.radial_basis.Kernels;
import core.nextlevelrl.radial_basis.RbfNetwork;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.function.DoubleSupplier;

public class TestRbfNetwork1D {

    public static final double LEARNING_RATE = 0.5;
    public static final double TOL = 0.2;
    public static final int N_FITS = 100;
    public static final DoubleSupplier INIT_VALUE_SUPPLIER = () -> 0.0;
    RbfNetwork rbfNetwork;
    TrainData data;

    @BeforeEach
    void init() {
        rbfNetwork = createNet();
        data = createData();
        rbfNetwork.fit(data, N_FITS);
    }

    @Test
    void whenTrained_thenCorrectOutput() {
        double out0 = rbfNetwork.outPut(data.input(0));
        double out1 = rbfNetwork.outPut(data.input(1));
        Assertions.assertEquals(data.output(0), out0, TOL);
        Assertions.assertEquals(data.output(1), out1, TOL);
    }

    @Test
    void whenCopiedWeights_thenCorrectOutput() {
        var rbfNetworkCopy = createNet();
        rbfNetworkCopy.copyWeights(rbfNetwork);
        double out0 = rbfNetworkCopy.outPut(data.input(0));
        double out1 = rbfNetworkCopy.outPut(data.input(1));
        Assertions.assertEquals(data.output(0), out0, TOL);
        Assertions.assertEquals(data.output(1), out1, TOL);
    }


    private TrainData createData() {
        data=TrainData.empty();
        data.add(new double[]{0.0}, 1.0);
        data.add(new double[]{1.0}, 2.0);
        return data;
    }

    private RbfNetwork createNet() {
        Kernels kernels = Kernels.empty();
        Kernel kernel1 = Kernel.ofSigmas(new double[]{0.0}, new double[]{1.0});
        Kernel kernel2 = Kernel.ofSigmas(new double[]{1.0}, new double[]{1.0});
        kernels.addKernel(kernel1);
        kernels.addKernel(kernel2);
        return RbfNetwork.of(kernels, LEARNING_RATE, 2);
    }


}
