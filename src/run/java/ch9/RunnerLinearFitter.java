package ch9;

import chapters.ch9.factory.TrainDataLinearFitterFactory;
import chapters.ch9.gradient_descent.LinearFitter;
import chapters.ch9.gradient_descent.PhiExtractor;
import core.foundation.config.ConfigFactory;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.collections.ListCreatorUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;
import core.plotting_core.plotting_2d.ChartUtility;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import core.plotting_core.plotting_2d.ScatterWithLineChartCreator;
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

    public static void main(String[] args) {
        var data = TrainDataLinearFitterFactory.getTrainData(MIN_X,MAX_X);
        var fitter = LinearFitter.of(data, getPhiExtractor(), LEARNING_RATE);
        var errors = fitter.fitAndReturnErrorPerEpoch(N_EPOCHS, BATCH_SIZE);
        fitter.logTimer();
        plotting(fitter, data, errors);
    }

    private static List<Double> getOutListFromFitter(List<Double> inList, LinearFitter fitter) {
        List<List<Double>> listOfLists = new ArrayList<>();
        inList.forEach(x -> listOfLists.add(List.of(x)));
        return fitter.calcOutputs(listOfLists);
    }

    private static PhiExtractor getPhiExtractor() {
        var phiExtractor = PhiExtractor.empty();
        phiExtractor.addFunction(x -> 1);
        phiExtractor.addFunction(x -> x.get(0));
        return phiExtractor;
    }

    private static void plotting(LinearFitter fitter, TrainData data, List<Double> errors) {
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


    private static XYChart getLineChartCreator(List<Double> errors) {
        var plotCfg = ConfigFactory.plotConfig();
        System.out.println("errors.size() = " + errors.size());
        List<Double> xData = ListCreatorUtil.createFromStartToEndWithNofItems(1, errors.size(), errors.size());
        var errorChartCreator = ManyLinesChartCreator.of(
                PlotSettings.ofDefaults()
                        .withWidth(plotCfg.xyChartWidth2Col()).withHeight(plotCfg.xyChartHeight())
                        .withXAxisLabel("Iteration").withYAxisLabel("Loss")
                        .withShowLegend(false),
                xData);
        errorChartCreator.addLine("Error", errors);
        return errorChartCreator.create();
    }

    private static XYChart getScatterWithLineChartCreator(
            TrainData data, List<Double> inList,
            List<Double> outList) {
        var plotCfg = ConfigFactory.plotConfig();
        var chartCreator = ScatterWithLineChartCreator.of(PlotSettings.ofDefaults()
                .withWidth(plotCfg.xyChartWidth2Col())
                .withHeight(plotCfg.xyChartHeight()));
        chartCreator.addLine(inList, outList);
        chartCreator.addScatter(data.inputsAsListList().stream()
                .map(x -> x.get(0)).toList(), data.outputs().stream().toList());
        return chartCreator.create();
    }



}
