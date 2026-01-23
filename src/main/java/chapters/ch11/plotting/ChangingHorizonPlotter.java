package chapters.ch11.plotting;

import core.foundation.util.collections.ListConverterUtil;
import core.foundation.util.collections.ListCreatorUtil;
import core.foundation.util.collections.ListUtil;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;
import lombok.SneakyThrows;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangingHorizonPlotter {

    public static final String FILE_NAME_N_STEP_EVAL = "lunar_step_horizon_evaluator.png";

    public record ResultsNStep(Map<Integer, List<Double>> resultMap) {
        public static ResultsNStep empty() {
            return new ResultsNStep(new HashMap<>());
        }

        public void add(int nSteps, double sumRewardAverage) {
            resultMap.computeIfAbsent(nSteps, k -> ListCreatorUtil.emptyDouble()).add(sumRewardAverage);
        }

        public double average(int nSteps) {
            return ListUtil.findAverage(resultMap.get(nSteps)).orElseThrow();
        }

        public double maxMinusMin(int nSteps) {
            double max = ListUtil.findMax(resultMap.get(nSteps)).orElseThrow();
            double min = ListUtil.findMin(resultMap.get(nSteps)).orElseThrow();
            return max - min;
        }
    }

    @SneakyThrows
    public static void plotNStepResults(ResultsNStep results, List<Integer> nStepsList) {
        var xList = ListConverterUtil.integer2Double(nStepsList);
        var yList = nStepsList.stream()
                .map(nSteps -> results.average(nSteps)).toList();
        var errList = nStepsList.stream()
                .map(nSteps -> results.maxMinusMin(nSteps)).toList();
        var chart = new XYChartBuilder()
                .width(600).height(200)
                .title("").xAxisTitle("N - Step horizon").yAxisTitle("Average return")
                .build();
        var styler = chart.getStyler();
        styler.setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        styler.setChartTitleVisible(false);
        styler.setLegendVisible(false);
        styler.setAxisTitlesVisible(true);
        styler.setXAxisDecimalPattern("0");
        styler.setChartBackgroundColor(Color.WHITE);
        XYSeries series = chart.addSeries("a", xList, yList, errList);
        series.setMarkerColor(Color.GRAY);
        series.setMarker(SeriesMarkers.SQUARE);
        ChartSaverAndPlotter.showChartSaveInFolderActorCritic(chart,FILE_NAME_N_STEP_EVAL);
    }


}
