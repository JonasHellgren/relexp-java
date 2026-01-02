package ch9;

import chapters.ch9.neural.core.DataGenerator;
import chapters.ch9.neural.core.NeuralNetBuilder;
import chapters.ch9.neural.plotting.ErrorBandPlotterNeuralOneDimRegression;
import chapters.ch9.neural.plotting.MeasuresOneDimRegressionNeural;
import chapters.ch9.neural.plotting.MeasuresOneDimRegressionNeuralEnum;
import chapters.ch9.neural.plotting.NeuralOneDimRegressionRecorder;
import core.foundation.config.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import core.foundation.util.cond.Conditionals;
import core.foundation.util.list_array.Array2ListConverter;
import core.foundation.util.list_array.List2ArrayConverter;
import core.foundation.util.list_array.ListCreator;
import core.nextlevelrl.MultiLayerPrinter;
import core.plotting.base.shared.PlotSettings;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class RunnerTrainerNeuralOneDimensional {

    public static final double MAX_X = 10d;
    public static final int N_ITEMS_PLOTTING = 100;
    public static final int BATCH_SIZE = 10;
    public static final int N_ITER = 100, N_EPOCHS_BETWEEN_LOGGING = 10;
    public static final int N_SAMPLES = 100;
    public static final double[] EVAL_IN = {1.0, 5.0, 7.5, 10.0};
    public static final int N_WINDOWS = 10;
    public static final String SUB_PATH = "neural/";
    public static final int IDX_IN1 = 0;
    public static final int IDX_IN10 = 3;

    static MultiLayerNetwork model;

    @SneakyThrows
    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        INDArray[] data = DataGenerator.generateData(N_SAMPLES);
        var dataset = new DataSet(data[0], data[1]);
        model = NeuralNetBuilder.buildWillWork();
        //model = NeuralNetBuilder.buildWillFail();
        var recorder = fitModel(dataset);
        timer.printInMs();
        var inPlotting = ListCreator.createFromStartToEndWithNofItems(0d, MAX_X, N_ITEMS_PLOTTING);
        var chart = getChartCorrelation(inPlotting, "Neural prediction");
        ChartSaverAndPlotter.showChartSaveInFolderAdvConceptsNeural(chart, "neural-1d");
        lossPlotting(recorder);
        MultiLayerPrinter.printWeights(model);
    }

    static void lossPlotting(NeuralOneDimRegressionRecorder recorder) throws IOException {
        var path = ProjectPropertiesReader.create().pathAdvConcepts()+ SUB_PATH;
        var plotter = ErrorBandPlotterNeuralOneDimRegression.ofFiltering(
                recorder,
                path,
                "neural_1d-error",
                N_WINDOWS);
        plotter.plotAndSave(List.of(
                MeasuresOneDimRegressionNeuralEnum.LOSS,
                MeasuresOneDimRegressionNeuralEnum.PRED1,
                MeasuresOneDimRegressionNeuralEnum.PRED10));
    }


    private static NeuralOneDimRegressionRecorder fitModel(DataSet dataset) {
        var recorder = NeuralOneDimRegressionRecorder.empty();
        var iterator = new ListDataSetIterator<>(dataset.asList(), BATCH_SIZE);
        for (int i = 0; i < N_ITER; i++) {
            iterator.reset();
            model.fit(iterator);
            maybeLog(i);
            var output = getNetOutput(EVAL_IN);
            var measures = MeasuresOneDimRegressionNeural.builder()
                    .error(model.score())
                    .valueLeft(output.getDouble(IDX_IN1))
                    .valueRight(output.getDouble(IDX_IN10))
                    .build();
            recorder.add(measures);
        }
        return recorder;
    }

    private static void maybeLog(int iter) {
        Conditionals.executeIfTrue(iter % N_EPOCHS_BETWEEN_LOGGING == 0, () -> {
            System.out.println("Iteration=" + iter + " → Predicted y for x=5, 7.5, 10: \n" + getNetOutput(EVAL_IN));
        });
    }

    private static INDArray getNetOutput(double[] evalIn) {
        var inAsIndArray = Nd4j.create(evalIn, new int[]{evalIn.length, 1});
        return model.output(inAsIndArray, false);
    }


    @SneakyThrows
    private static XYChart getChartCorrelation(List<Double> inPlotting, String titleRbf) {
        var weight = ProjectPropertiesReader.create().xyChartWidth2Col();
        var height = ProjectPropertiesReader.create().xyChartHeight();
        var chartCreator = ManyLinesChartCreator.of(
                PlotSettings.ofDefaults()
                        .withWidth(weight).withHeight(height)
                        .withLegendPosition(Styler.LegendPosition.InsideSE)
                        .withColorRangeSeries(new Color[]{Color.BLACK, Color.GRAY}),
                inPlotting);
        var outRef = inPlotting.stream().toList();
        double[] inArr = List2ArrayConverter.convertListToDoubleArr(inPlotting);
        INDArray output = getNetOutput(inArr);
        var outNeuralList = Array2ListConverter.arrayToList(output.toDoubleVector());
        chartCreator.addLine("Ref.", outRef);
        chartCreator.addLine(titleRbf, outNeuralList);
        return chartCreator.create();
    }


}
