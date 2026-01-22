package ch2;


import chapters.ch2.factory.FittingParametersFactory;
import core.foundation.config.ConfigFactory;
import core.foundation.util.math.SigmoidFunctionsUtil;
import core.plotting_rl.chart.ChartCreatorFactory;
import core.plotting_core.chart_plotting.ChartSaver;
import core.plotting_core.plotting_2d.ChartUtility;

import java.util.List;


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
        ChartSaver.saveAndShowXYChart(chart, ConfigFactory.pathPicsConfig().ch2(),FILE_NAME);
    }

    private static List<Double> getyList(List<Double> xList) {
        return xList.stream()
                .mapToDouble(SigmoidFunctionsUtil.sigmoid::applyAsDouble)
                .boxed().toList();
    }



}
