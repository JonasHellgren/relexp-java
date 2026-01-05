package ch3;

import core.foundation.configOld.PathAndFile;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.ListCreator;
import core.foundation.util.math.LogarithmicDecay;
import core.plotting.base.shared.PlotSettings;
import core.plotting.chart_plotting.ChartSaver;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import java.io.IOException;
import java.util.List;

public class RunnerLogarithmicDecay {

    static final double OUT_END = 0.01;
    static final int ITERATION_END = 10_001;
    public static final List<Double> START_LIST = List.of(90d, 50d, 10d);

    public static void main(String[] args) throws IOException {
        var xList = getIterationList();
        var reader = ProjectPropertiesReader.create();
        var creator = getChartCreator(reader, xList);
        fillCreatorWithLineData(xList, creator);
        var chart = creator.create();
        plotAndSave(chart,reader);
    }

    private static List<Double> getIterationList() {
        return ListCreator.createFromStartWithStepWithNofItems(0d, 1.0d, ITERATION_END);
    }

    private static void plotAndSave(XYChart chart, ProjectPropertiesReader reader) {
        var pathPics = reader.pathConceptsPics();
        ChartSaver.saveXYChart(chart, PathAndFile.ofPng(pathPics, "logarithmic_decay"));
        new SwingWrapper<>(chart).displayChart();
    }

    private static void fillCreatorWithLineData(List<Double> iterationList,
                                                ManyLinesChartCreator creator) {
        for (double start : START_LIST) {
            var decay = LogarithmicDecay.of(start, OUT_END, ITERATION_END);
            var outList = iterationList.stream().map(decay::calcOut).toList();
            creator.addLine(String.valueOf(start), outList);
        }
    }

    private static ManyLinesChartCreator getChartCreator(ProjectPropertiesReader reader,
                                                         List<Double> iterationList) {
        return ManyLinesChartCreator.of(PlotSettings.ofDefaults()
                .withXAxisLabel("Iteration").withYAxisLabel("Learning rate (%)")
                .withSpaceBetweenXTicks(5000d)
                .withWidth(reader.xyChartWidth2Col()).withHeight(reader.xyChartHeight()), iterationList);
    }

}
