package ch2;


import chapters.ch2.factory.FittingParametersFactory;
import core.foundation.util.math.SigmoidFunctions;
import core.plotting.chart_plotting.ChartCreatorFactory;
import core.plotting.plotting_2d.ChartUtility;

import java.util.List;

import static core.plotting.chart_plotting.ChartSaverAndPlotter.showChartSaveInFolderConcepts;


public class RunnerPlotSigma {
    public static final int N_POINTS = 1000;
    public static final String FILE_NAME = "sigmoid";

    public static void main(String[] args) {
        var parameters = FittingParametersFactory.produceDefault();
        var xList= parameters.getXList(N_POINTS);
        var chartCreator = ChartCreatorFactory.produceXYLine();
        chartCreator.addLine(xList, getyList(xList));
        var chart = chartCreator.create();
        ChartUtility.reduceXAxisTicksClutter(chart,2, "0");
        showChartSaveInFolderConcepts(chart, FILE_NAME);
    }

    private static List<Double> getyList(List<Double> xList) {
        return xList.stream()
                .mapToDouble(SigmoidFunctions.sigmoid::applyAsDouble)
                .boxed().toList();
    }



}
