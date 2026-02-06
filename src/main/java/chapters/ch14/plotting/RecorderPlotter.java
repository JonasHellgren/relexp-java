package chapters.ch14.plotting;

import com.google.common.base.Preconditions;
import core.foundation.config.PathAndFile;
import core.foundation.config.PlotConfig;
import core.foundation.util.collections.ArrayCreatorUtil;
import core.foundation.util.collections.List2ArrayConverterUtil;
import core.plotting_core.chart_saving_and_plotting.ChartSaver;
import core.plotting_core.plotting_2d.StairDataGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;
import java.awt.*;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecorderPlotter {

    static final String X_LABEL = "Trial";
    private final Recorder recorder;
    private final String filePath;
    private final String fileNameAddOn;
    private final PlotConfig plotConfig;

    public static RecorderPlotter of(Recorder recorder,
                                     String filePath,
                                     String fileNameAddOn,
                                     PlotConfig plotConfig) {
        return new RecorderPlotter(recorder, filePath, fileNameAddOn, plotConfig);
    }

    public void plotAndSave(List<MeasuresCombLPEnum> measures) {
        Preconditions.checkArgument(!recorder.isEmpty(), "No training progress data to plot");
        measures.forEach(measure -> showAndSavePlot(measure));
    }

    private void showAndSavePlot(MeasuresCombLPEnum measure) {
        var yData = List2ArrayConverterUtil.convertListToDoubleArr(recorder.trajectory(measure));
        var xData = ArrayCreatorUtil.createArrayInRange(0, 1, yData.length - 1);
        var xyDataStair = StairDataGenerator.generateWithEndStep(Pair.create(xData, yData));
        var chart = new XYChartBuilder()
                .width(plotConfig.xyChartWidth2Col()).height(plotConfig.xyChartHeight())
                .title(" ").xAxisTitle(X_LABEL).yAxisTitle(measure.description).build();
        chart.getStyler()
                .setLegendVisible(false)
                .setChartBackgroundColor(Color.white)
                .setSeriesColors(new Color[]{Color.black});
        chart.addSeries("Stairs", xyDataStair.getFirst(), xyDataStair.getSecond()).setMarker(SeriesMarkers.NONE);
        ChartSaver.saveAndShowXYChart(chart, PathAndFile.ofPng(filePath, measure + fileNameAddOn));
    }

}
