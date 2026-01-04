package chapters.ch2.plotting;

import chapters.ch2.domain.parameter_fitting.LearningRateFittingResults;
import core.foundation.config.PathAndFile;
import core.foundation.config.ProjectPropertiesReader;
import core.plotting.chart_plotting.ChartSaver;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import org.knowm.xchart.SwingWrapper;

import java.io.IOException;

public class SingleParameterFittingPlotter {

    public static final String FILE_NAME = "fitterSingleParameter";

    public static void saveAndPlot(ManyLinesChartCreator creator) throws IOException {
        var chart = creator.create();
        var props = ProjectPropertiesReader.create();
        PathAndFile pathAndFile = PathAndFile.ofPng(
                props.getStringProperty("concepts_pics"), FILE_NAME);
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
