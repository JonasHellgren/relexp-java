package ch9;

import chapters.ch9.factory.Radial3dFactory;
import core.foundation.config.ConfigFactory;
import core.foundation.gadget.timer.CpuTimer;
import core.nextlevelrl.radial_basis.RbfNetwork;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;
import core.plotting_core.plotting_3d.HeatMapChartCreator;
import lombok.SneakyThrows;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

class RunnerRadialBasis3dFunction {
    static final double F_MAX = 10.0;
    static final int N_FITS = 500;
    static final double LEARNING_RATE = 0.9;
    static RbfNetwork rbfn;

    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        DoubleBinaryOperator fcn = (x, y) -> F_MAX * Math.sin((Math.PI / 3) * x * (7 - y));
        var kernels = Radial3dFactory.createKernels();
        rbfn = RbfNetwork.of(kernels, LEARNING_RATE,2);
        var trainData = Radial3dFactory.createTrainData(fcn);
        rbfn.fit(trainData, N_FITS);
        timer.printInMs();
        showAndSaveData(Radial3dFactory.createArrayData(fcn), "Reference HeatMap");
        DoubleBinaryOperator fcnRbn = (x, y) -> rbfn.outPutListIn(List.of(x, y));
        showAndSaveData(Radial3dFactory.createArrayData(fcnRbn), "RBF HeatMap");
    }

    @SneakyThrows
    private static void showAndSaveData(double[][] data, String title) {
        var plotCfg = ConfigFactory.plotConfig();
        var settings = PlotSettings.defaultBuilder()
                .title(title).showDataValues(false)
                .width(plotCfg.width()).height(plotCfg.height())
                .showAxisTicks(true).build();
        var creator = HeatMapChartCreator.of(
                settings,
                data,
                Radial3dFactory.getXData(),
                Radial3dFactory.getYData());
        ChartSaverAndPlotter.showAndSaveHeatMapRbf(creator.create(), title);
    }



}
