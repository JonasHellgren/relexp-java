package ch9;

import chapters.ch9.radial_basis.Kernel;
import chapters.ch9.radial_basis.Kernels;
import chapters.ch9.radial_basis.RbfNetwork;
import core.foundation.config.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.collections.ArrayCreator;
import core.foundation.util.collections.ListCreator;
import core.plotting.base.shared.PlotSettings;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import core.plotting.plotting_3d.HeatMapChartCreator;
import lombok.SneakyThrows;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

class RunnerRadialBasis3dFunction {

    static final int LENGTH = 50;
    static final double F_MAX = 10.0;
    static final int N_EPOCHS = 200;
    static final int BATCH_LEN = 10;
    static final int N_KERNELS_EACH_DIM = 20;  //10 20
    static final double K_SIGMA = 0.5;
    static final double LEARNING_RATE = 0.9;
    static RbfNetwork rbfn;

    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        DoubleBinaryOperator fcn = (x, y) -> F_MAX * Math.sin((Math.PI / 3) * x * (7 - y));
        var kernels = createKernels();
        rbfn = RbfNetwork.of(kernels, LEARNING_RATE);
        var trainData = createTrainData(fcn);
        rbfn.fit(trainData,N_EPOCHS,BATCH_LEN);
        timer.printInMs();
        showAndSaveData(createArrayData(fcn), "Reference HeatMap");
        DoubleBinaryOperator fcnRbn = (x, y) -> rbfn.outPut(List.of(x, y));
        showAndSaveData(createArrayData(fcnRbn), "RBF HeatMap");
    }

    private static Kernels createKernels() {
        var kernels = Kernels.empty();
        double xMin = getXData()[0];
        double xMax = getXData()[getXData().length - 1];
        double yMin = getYData()[0];
        double yMax = getYData()[getYData().length - 1];
        double sigmaX = K_SIGMA * ((xMax - xMin) / (N_KERNELS_EACH_DIM - 1));
        double sigmaY = K_SIGMA * ((yMax - yMin) / (N_KERNELS_EACH_DIM - 1));
        var xList = ListCreator.createFromStartToEndWithNofItems(xMin, xMax, N_KERNELS_EACH_DIM);
        var yList = ListCreator.createFromStartToEndWithNofItems(yMin, yMax, N_KERNELS_EACH_DIM);
        for (double x : xList) {
            for (double y : yList) {
                kernels.addKernel(Kernel.ofSigmas(new double[]{x, y}, new double[]{sigmaX, sigmaY}));
            }
        }
        return kernels;
    }

    private static TrainData createTrainData(DoubleBinaryOperator fcn) {
        var trainData = TrainData.emptyFromOutputs();
        for (double x : getXData()) {
            for (double y : getYData()) {
                trainData.addIAndOut(List.of(x, y), fcn.applyAsDouble(x, y));
            }
        }
        return trainData;
    }

    @SneakyThrows
    private static void showAndSaveData(double[][] data, String title) {
        var weight= ProjectPropertiesReader.create().xyChartWidth2Col();
        var settings = PlotSettings.defaultBuilder()
                .title(title).showDataValues(false)
                .width(weight).height(250)
                .showAxisTicks(true).build();
        var creator = HeatMapChartCreator.of(settings, data, getXData(), getYData());
        ChartSaverAndPlotter.showAndSaveHeatMapRbf(creator.create(), title);
    }

    private static double[][] createArrayData(DoubleBinaryOperator fcn) {
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

    private static double[] getXData() {
        return ArrayCreator.createArrayFromStartAndEnd(LENGTH, -3, 3);
    }

    private static double[] getYData() {
        return ArrayCreator.createArrayFromStartAndEnd(LENGTH, 0, 7);
    }


}
