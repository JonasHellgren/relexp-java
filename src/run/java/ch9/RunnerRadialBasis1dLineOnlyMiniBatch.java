package ch9;

import chapters.ch9.radial_basis.Kernels;
import chapters.ch9.radial_basis.RbfNetwork;
import core.foundation.config.ProjectPropertiesReader;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.collections.Array2ListConverter;
import core.foundation.util.collections.ArrayCreator;
import core.foundation.util.collections.ListCreator;
import core.plotting.base.shared.PlotSettings;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@Log
public class RunnerRadialBasis1dLineOnlyMiniBatch {

    public static final double MAX_X = 10d;
    public static final double MAX_Y = 10d;
    public static final int N_KERNELS = 6;
    public static final double SIGMA = 0.5 * (MAX_X / (N_KERNELS - 1));
    public static final double LEARNING_RATE = 0.1;
    public static final int N_EPOCHS = 1000;
    public static final int N_ITEMS_PLOTTING = 100;
    public static final int BATCH_SIZE = 10;
    static RbfNetwork rbBatch;
    static List<Double> inTraining;
    static List<List<Double>> inTrainingList;
    static List<Double> outTrainingList;
    static List<Double> inPlotting;
    static List<Double> outPlottingListRef;

    public static void main(String[] args) {
        init();
        fitWeights(rbBatch);
        showCorrelationChart(getOutPlottingRbfList(rbBatch), "RBF","RBF-1d-correlection");
        showKernelChart(rbBatch, " ","RBF-1d-weights-centers");
    }

    private static void init() {
        double[] centers = ArrayCreator.createArrayFromStartAndEndWithNofItems(0d, MAX_X, N_KERNELS);
        double[] sigmas = ArrayCreator.createArrayWithSameDoubleNumber(N_KERNELS, SIGMA);
        var kernels = Kernels.empty();
        kernels.addKernelsWithCentersAndSigmas(centers, sigmas);
        rbBatch = RbfNetwork.of(kernels, LEARNING_RATE);
        inTraining = ListCreator.createFromStartToEndWithNofItems(0d, MAX_X, BATCH_SIZE);
        inTrainingList = inTraining.stream().map(in -> List.of(in)).toList();
        outTrainingList = ListCreator.createFromStartToEndWithNofItems(0d, MAX_Y, BATCH_SIZE);
        outPlottingListRef = ListCreator.createFromStartToEndWithNofItems(0d, MAX_Y, N_ITEMS_PLOTTING);
        inPlotting = ListCreator.createFromStartToEndWithNofItems(0d, MAX_X, N_ITEMS_PLOTTING);
    }


    private static void showCorrelationChart(List<Double> outRbfList, String name, String fileName) {
        var chart = getChartCorrelation(name, outRbfList);
        ChartSaverAndPlotter.showChartSaveInFolderRbf(chart, fileName);
    }


    private static void showKernelChart(RbfNetwork rb1, String titleRbf, String fileName) {
        var chartRbf = getChartRbf(titleRbf);
        var xData = getXData();
        var yData = Array2ListConverter.arrayToList(rb1.getWeights().getArray());
        chartRbf.addSeries(titleRbf, xData, yData);
        ChartSaverAndPlotter.showChartSaveInFolderRbf(chartRbf, fileName);
    }

    static void fitWeights(RbfNetwork rb) {
        var data = TrainData.emptyFromOutputs();
        for (int i = 0; i < inTrainingList.size(); i++) {
            data.addIAndOut(inTrainingList.get(i), outTrainingList.get(i));
        }
        rb.fit(data, N_EPOCHS,BATCH_SIZE);  // int nEpochs, int batchSize
    }


    private static List<Double> getXData() {
        List<Double> xData = new LinkedList<>();
        for (int i = 0; i < rbBatch.nKernels(); i++) {
            xData.add(rbBatch.getKernels().get(i).centerCoordinates()[0]);
        }
        return xData;
    }


    @SneakyThrows
    private static XYChart getChartRbf(String titleRbf) {
        var weight= ProjectPropertiesReader.create().xyChartWidth2Col();
        var height=ProjectPropertiesReader.create().xyChartHeight();
        var chartRbf = new XYChartBuilder()
                .title(titleRbf).xAxisTitle("x").yAxisTitle("w")
                .width(weight).height(height).build();
        var styler = chartRbf.getStyler();
        styler.setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        styler.setChartTitleVisible(true);
        styler.setLegendVisible(false);
        styler.setChartBackgroundColor(Color.WHITE);
        styler.setMarkerSize(5);
        return chartRbf;
    }


    private static List<Double> getOutPlottingRbfList(RbfNetwork rb) {
        return inPlotting.stream().map(in -> rb.outPut(List.of(in))).toList();
    }


    @SneakyThrows
    private static XYChart getChartCorrelation(String titleRbf, List<Double> outRbfList) {
        var weight=ProjectPropertiesReader.create().xyChartWidth2Col();
        var height=ProjectPropertiesReader.create().xyChartHeight();
        var chartCreator = ManyLinesChartCreator.of(
                PlotSettings.ofDefaults()
                        .withWidth(weight).withHeight(height)
                        .withLegendPosition(Styler.LegendPosition.InsideSE)
                        .withColorRangeSeries(new Color[]{Color.BLACK, Color.GRAY}),
                inPlotting);
        chartCreator.addLine("Ref", outPlottingListRef);
        chartCreator.addLine(titleRbf, outRbfList);
        return chartCreator.create();
    }



}
