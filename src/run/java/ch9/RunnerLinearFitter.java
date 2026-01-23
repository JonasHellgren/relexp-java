package ch9;

import chapters.ch9.gradient_descent.LinearFitter;
import chapters.ch9.gradient_descent.PhiExtractor;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.TrainDataOld;
import core.foundation.util.collections.ListCreatorUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;
import core.plotting_core.plotting_2d.ChartUtility;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import core.plotting_core.plotting_2d.ScatterWithLineChartCreator;
import lombok.SneakyThrows;
import org.knowm.xchart.XYChart;
import java.util.ArrayList;
import java.util.List;

public class RunnerLinearFitter {

    public static final int N_ITEMS_LINE = 100;
    public static final double LEARNING_RATE = 0.1;
    public static final int N_EPOCHS = 100;
    public static final double MIN_X = 0.0;
    public static final double MAX_X = 5.0;
    public static final int BATCH_SIZE = 1;  //1 or 5

    @SneakyThrows
    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        var data = getTrainData();
        var fitter = LinearFitter.of(data, getPhiExtractor(), LEARNING_RATE);
        var errors = fitter.fitAndReturnErrorPerEpoch(N_EPOCHS, BATCH_SIZE);
        timer.printInMs();
        var xLineList = ListCreatorUtil.createFromStartToEndWithNofItems(MIN_X, MAX_X, N_ITEMS_LINE);
        var yLineList = getOutListFromFitter(xLineList, fitter);
        var scatterChart = getScatterWithLineChartCreator(data, xLineList, yLineList);
        scatterChart.getStyler().setXAxisTickMarkSpacingHint(10);
        var errorChart = getLineChartCreator(errors);
        ChartUtility.reduceXAxisTicksClutter(errorChart,10, "0");
        ChartSaverAndPlotter.showChartSaveInFolderGradientLearning(scatterChart, "scatterAndLine" + BATCH_SIZE);
        ChartSaverAndPlotter.showChartSaveInFolderGradientLearning(errorChart, "errorChart" + BATCH_SIZE);
        System.out.println("fitter.getWeights() = " + fitter.getWeights());
    }

    @SneakyThrows
    private static XYChart getLineChartCreator(List<Double> errors) {
        var weight = ProjectPropertiesReader.create().xyChartWidth2Col();
        var height = ProjectPropertiesReader.create().xyChartHeight();
        System.out.println("errors.size() = " + errors.size());
        List<Double> xData = ListCreatorUtil.createFromStartToEndWithNofItems(1, errors.size(), errors.size());
        var errorChartCreator = ManyLinesChartCreator.of(
                PlotSettings.ofDefaults()
                        .withWidth(weight).withHeight(height)
                        .withXAxisLabel("Iteration").withYAxisLabel("Loss")
                        .withShowLegend(false),
                xData);
        errorChartCreator.addLine("Error", errors);
        return errorChartCreator.create();
    }

    @SneakyThrows
    private static XYChart getScatterWithLineChartCreator(
            TrainData data, List<Double> inList,
            List<Double> outList) {
        var weight = ProjectPropertiesReader.create().xyChartWidth2Col();
        var height = ProjectPropertiesReader.create().xyChartHeight();
        var chartCreator = ScatterWithLineChartCreator.of(PlotSettings.ofDefaults()
                .withWidth(weight).withHeight(height));
        chartCreator.addLine(inList, outList);
        chartCreator.addScatter(data.inputsAsListList().stream()
                .map(x -> x.get(0)).toList(), data.outputs().stream().toList());
        return chartCreator.create();
    }

    private static List<Double> getOutListFromFitter(List<Double> inList, LinearFitter fitter) {
        List<List<Double>> listOfLists = new ArrayList<>();
        inList.forEach(x -> listOfLists.add(List.of(x)));
        return fitter.calcOutputs(listOfLists);
    }

    private static PhiExtractor getPhiExtractor() {
        var phiExtractor = PhiExtractor.empty();
        phiExtractor.functionList.add(x -> 1);
        phiExtractor.functionList.add(x -> x.get(0));
        return phiExtractor;
    }

    private static TrainData getTrainData() {
        var data = TrainData.empty();
        var outList = List.of(
                4.5 + 1.0,
                4.2 + 1.0,
                5.2 + 2.0,
                5.4 + 2.0,
                5.0 + 3.0,
                5.6 + 3.0,
                5.0 + 4.0,
                5.2 + 4.0,
                5.3 + 5.0,
                5.6 + 5.0,
                5.0 + 6.0,
                5.2 + 6.0,
                5.0 + 7.0,
                5.1 + 7.0,
                4.7 + 8.0,
                4.9 + 8.0,
                4.9 + 9.0,
                4.7 + 9.0,
                5.2 + 10.0,
                5.0 + 10.0
        );

        var inList = ListCreatorUtil.createFromStartToEndWithNofItems(MIN_X, MAX_X, outList.size());
        inList.stream().forEach(in ->
                data.addListIn(List.of(in), outList.get(inList.indexOf(in))));
        return data;
    }

    private static TrainDataOld getTrainData0() {
        var data = TrainDataOld.emptyFromOutputs();
        var inList = ListCreatorUtil.createFromStartToEndWithNofItems(MIN_X, MAX_X, 10);
        var outList = List.of(
                4.5 + 1.0,
                5.2 + 2.0,
                5.0 + 3.0,
                5.0 + 4.0,
                5.3 + 5.0,
                5.0 + 6.0,
                5.0 + 7.0,
                4.7 + 8.0,
                4.9 + 9.0,
                5.0 + 10.0);
        inList.stream().forEach(in ->
                data.addIAndOut(List.of(in), outList.get(inList.indexOf(in))));
        return data;
    }

}
