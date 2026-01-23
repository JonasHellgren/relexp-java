package core.radial_basis;

import core.nextlevelrl.radial_basis.Kernel;
import core.nextlevelrl.radial_basis.Kernels;
import core.nextlevelrl.radial_basis.RbfNetwork;
import core.nextlevelrl.radial_basis.TrainData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestRbfNetwork2D {

    static final double LEARNING_RATE = 0.5;
    static final double TOL = 0.01;
    static final int N_FITS = 100;
    static final double SIGMA = 0.25;  //small radius of influence
    static final double NO_FITTED_VALUE = 0.0;
    RbfNetwork rbfNetwork;
    TrainData data;

    @BeforeEach
    void init() {
        createNet();
        createData();
    }

    private void createData() {
        data=TrainData.empty();
        double[] input1 = new double[]{0.0,0.0};
        double[] input2 = new double[]{1.0,0.0};
        data.addIAndOut(input1, 1.0);
        data.addIAndOut(input2, 2.0);
        rbfNetwork.fit(data, N_FITS);
    }

    private void createNet() {
        Kernels kernels = Kernels.empty();
        Kernel kernel1 = Kernel.ofSigmas(new double[]{0.0,0.0}, new double[]{SIGMA,SIGMA});
        Kernel kernel2 = Kernel.ofSigmas(new double[]{1.0,0.0}, new double[]{SIGMA,SIGMA});
        Kernel kernel3 = Kernel.ofSigmas(new double[]{0.0,1.0}, new double[]{SIGMA,SIGMA});
        Kernel kernel4 = Kernel.ofSigmas(new double[]{1.0,1.0}, new double[]{SIGMA,SIGMA});
        kernels.addKernel(kernel1);
        kernels.addKernel(kernel2);
        kernels.addKernel(kernel3);
        kernels.addKernel(kernel4);
        rbfNetwork = RbfNetwork.of(kernels, LEARNING_RATE, 2);
    }

    @Test
    void whenTrained_thenCorrectOutput() {
        double out0 = rbfNetwork.outPut(data.input(0));
        double out1 = rbfNetwork.outPut(data.input(1));

        double[] input3 = new double[]{1.0,1.0};
        double out3 = rbfNetwork.outPut(input3);
        Assertions.assertEquals(data.output(0), out0, TOL);
        Assertions.assertEquals(data.output(1), out1, TOL);
        Assertions.assertEquals(NO_FITTED_VALUE, out3, TOL);
    }


}
