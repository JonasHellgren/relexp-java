package ch9;

import chapters.ch9.radial_basis.Kernels;
import chapters.ch9.radial_basis.RbfNetwork;
import core.foundation.util.collections.ArrayCreatorUtil;
import core.foundation.util.collections.ListCreatorUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import org.apache.commons.compress.utils.Lists;
import org.knowm.xchart.XYChart;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunnerPlotSingleRbf {

    public static final double MIN_X = -3d;
    public static final double MAX_X = 3d;  //trick to get correct x-axis ticks, 5.5
    public static final double MAX_Y = 1d;
    public static final List<Double> SIGMAS = List.of(0.5, 1d, 2d);
    public static final int WIDTH = 400;
    public static final int HEIGHT = 150;
    public static final int N_POINTS = 50;

    public static void main(String[] args) {
        var inList = ListCreatorUtil.createFromStartToEndWithNofItems(MIN_X, MAX_X, N_POINTS);
        var sigmIdx2OutListMap = getPlotDataMap(inList);
        var chart = getChart(inList, sigmIdx2OutListMap);
        ChartSaverAndPlotter.showChartSaveInFolderRbf(chart, "single_rbf");
    }

    static Map<Integer, List<Double>> getPlotDataMap(List<Double> inList) {
        Map<Integer, List<Double>> sigmIdx2OutListMap = new HashMap<>();
        for (double sigma : SIGMAS) {
            sigmIdx2OutListMap.put(SIGMAS.indexOf(sigma), getOutList(sigma, inList));
        }
        return sigmIdx2OutListMap;
    }


    private static XYChart getChart(List<Double> inList, Map<Integer, List<Double>> sigmIdx2OutListMap) {
        var chartCreator = ManyLinesChartCreator.of(
                PlotSettings.ofDefaults()
                        .withWidth(WIDTH).withHeight(HEIGHT)
                        .withYAxisLabel("Activation")
                        .withAxisTicksDecimalFormat("#.#")
                        .withColorRangeSeries(new Color[]{Color.BLACK, Color.GRAY, Color.LIGHT_GRAY}),
                inList);
        for (double sigma : SIGMAS) {
            var outList = sigmIdx2OutListMap.get(SIGMAS.indexOf(sigma));
            chartCreator.addLine("sigma=" + sigma, outList);
        }
        return chartCreator.create();
    }

    private static List<Double> getOutList(double sigma, List<Double> inList) {
        List<Double> outList = Lists.newArrayList();
        var rbfNetwork = createOneKernelRbf(sigma);
        for (double in : inList) {
            double out = rbfNetwork.outPut(List.of(in));
            outList.add(out);
        }
        return outList;
    }

    private static RbfNetwork createOneKernelRbf(double sigma) {
        double dummyLearningRate = 1d;
        int nKernels = 1;
        double[] centers = ArrayCreatorUtil.createArrayFromStartAndEndWithNofItems(0d, MAX_X, nKernels);
        double[] sigmas = ArrayCreatorUtil.createArrayWithSameDoubleNumber(nKernels, sigma);
        var kernels = Kernels.empty();
        kernels.addKernelsWithCentersAndSigmas(centers, sigmas);
        var rbf = RbfNetwork.of(kernels, dummyLearningRate);
        rbf.setWeights(ArrayCreatorUtil.createArrayWithSameDoubleNumber(1, MAX_Y));
        return rbf;
    }


}
