package chapters.ch9.factory;

import core.foundation.gadget.training.TrainData;
import core.foundation.util.collections.ArrayCreatorUtil;
import core.foundation.util.collections.ListCreatorUtil;
import core.nextlevelrl.radial_basis.Kernel;
import core.nextlevelrl.radial_basis.Kernels;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.DoubleBinaryOperator;

@UtilityClass
public class Radial3dFactory {

    static final int LENGTH = 50;
    static final int N_KERNELS_EACH_DIM = 20;  //10 20
    static final double K_SIGMA = 0.5;

    public static Kernels createKernels() {
        var kernels = Kernels.empty();
        double xMin = getXData()[0];
        double xMax = getXData()[getXData().length - 1];
        double yMin = getYData()[0];
        double yMax = getYData()[getYData().length - 1];
        double sigmaX = K_SIGMA * ((xMax - xMin) / (N_KERNELS_EACH_DIM - 1));
        double sigmaY = K_SIGMA * ((yMax - yMin) / (N_KERNELS_EACH_DIM - 1));
        var xList = ListCreatorUtil.createFromStartToEndWithNofItems(xMin, xMax, N_KERNELS_EACH_DIM);
        var yList = ListCreatorUtil.createFromStartToEndWithNofItems(yMin, yMax, N_KERNELS_EACH_DIM);
        for (double x : xList) {
            for (double y : yList) {
                kernels.addKernel(Kernel.ofSigmas(new double[]{x, y}, new double[]{sigmaX, sigmaY}));
            }
        }
        return kernels;
    }


    public static TrainData createTrainData(DoubleBinaryOperator fcn) {
        var trainData = TrainData.empty();
        for (double x : getXData()) {
            for (double y : getYData()) {
                trainData.addListIn(List.of(x, y), fcn.applyAsDouble(x, y));
            }
        }
        return trainData;
    }

    public static double[][] createArrayData(DoubleBinaryOperator fcn) {
        double[][] data = new double[LENGTH][LENGTH];
        for (int xi = 0; xi < getXData().length; xi++) {
            for (int yi = 0; yi < getYData().length; yi++) {
                double x = getXData()[xi];
                double y = getYData()[yi];
                data[yi][xi] = fcn.applyAsDouble(x, y);
            }
        }
        return data;
    }

    public static double[] getXData() {
        return ArrayCreatorUtil.createArrayFromStartAndEnd(LENGTH, -3, 3);
    }

    public static double[] getYData() {
        return ArrayCreatorUtil.createArrayFromStartAndEnd(LENGTH, 0, 7);
    }



}
