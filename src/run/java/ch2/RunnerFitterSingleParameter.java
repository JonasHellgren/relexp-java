package ch2;

import chapters.ch2.factory.FittingParametersFactory;
import chapters.ch2.domain.FittingParameters;
import chapters.ch2.impl.parameter_fitting.FitterSingleParameter;
import core.foundation.config.PathAndFile;
import core.foundation.config.ProjectPropertiesReader;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.collections.MyListUtils;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import core.plotting.chart_plotting.ChartSaver;
import core.plotting.chart_plotting.PlotterFactory;
import lombok.SneakyThrows;
import org.knowm.xchart.SwingWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class RunnerFitterSingleParameter {

    public static final double OUTPUT_TARGET = 1.0;
    static final List<Double> LEARNING_RATES = List.of(0.1, 0.2, 0.4, 0.8);
    public static final int NOF_ITERATIONS = 51;
    public static final String FILE_NAME = "fitterSingleParameter";
    public static final double SPACE_BETWEEN_X_TICKS = 10d;
    public static final int NOF_TIME0_ITEMS = 1;
    public static final double START_W = 0.0d;

    @SneakyThrows
    public static void main(String[] args) {
        var par0 = FittingParametersFactory.produceDefault()
                .withNofIterations(NOF_ITERATIONS);
        var factor = PlotterFactory.builder()
                .xLabel("Iteration").yLabel("w")
                .spaceBetweenXTicks(SPACE_BETWEEN_X_TICKS)
                .nItems(par0.nofIterations()+ NOF_TIME0_ITEMS)
                .build();
        var creator=factor.getManyLinesChartCreator();
        fillCreatorWithResults(getTrainingResults(par0), creator);
        saveAndPlot(creator);
    }

    private static void saveAndPlot(ManyLinesChartCreator creator) throws IOException {
        var chart = creator.create();
        var props = ProjectPropertiesReader.create();
        PathAndFile pathAndFile = PathAndFile.ofPng(
                props.getStringProperty("concepts_pics"), FILE_NAME);
        System.out.println("pathAndFile = " + pathAndFile);
        ChartSaver.saveXYChart(chart, pathAndFile);
        new SwingWrapper<>(chart).displayChart();
    }

    private static void fillCreatorWithResults(Map<Integer, List<Double>> results,
                                               ManyLinesChartCreator creator) {
        for (double learningRate : LEARNING_RATES) {
            var outputs = results.get(LEARNING_RATES.indexOf(learningRate));
            creator.addLine(Double.toString(learningRate), outputs);
        }
    }


    private static Map<Integer, List<Double>> getTrainingResults(FittingParameters par0) {
        Map<Integer, List<Double>> results = new HashMap<>();
        for (double learningRate : LEARNING_RATES) {
            var par1 = par0.withLearningRate(learningRate);
            var fitter = FitterSingleParameter.of(par1);
            var data = TrainData.emptyFromOutputs();
            var outputs0 = getOutputs(data, par0, fitter);
            var outputs = MyListUtils.merge(List.of(START_W),outputs0);
            results.put(LEARNING_RATES.indexOf(learningRate), outputs);
        }
        return results;
    }

    private static List<Double> getOutputs(TrainData data, FittingParameters par0, FitterSingleParameter fitter) {
        data.addIAndOut(List.of(0d), OUTPUT_TARGET);
        return IntStream.range(0, par0.nofIterations())
                .mapToObj(i -> {
                    fitter.fit(data);
                    return fitter.read(0d);
                }).toList();
    }

}
