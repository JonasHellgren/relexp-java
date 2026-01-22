package chapters.ch2.plotting;

import chapters.ch2.domain.parameter_fitting.LearningRateFittingResults;
import core.foundation.config.PathPicsConfig;
import core.foundation.config.PathAndFile;
import core.plotting.chart_plotting.ChartSaver;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import org.knowm.xchart.SwingWrapper;

public class SingleParameterFittingPlotter {


    public static void saveAndPlot(ManyLinesChartCreator creator, PathPicsConfig cfg, String fileName) {
        var chart = creator.create();
        PathAndFile pathAndFile = PathAndFile.ofPng(cfg.ch2(), fileName);
        ChartSaver.saveXYChart(chart, pathAndFile);
        new SwingWrapper<>(chart).displayChart();
    }


    public static void addResultToCreator(LearningRateFittingResults results,
                                          ManyLinesChartCreator creator,
                                          double learningRate) {
            var outputs = results.getOutputs(learningRate);
            creator.addLine(Double.toString(learningRate), outputs);
    }

}
