package chapters.ch14.plotting;

import com.google.common.base.Preconditions;
import core.foundation.config.PathAndFile;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.ArrayCreator;
import core.foundation.util.collections.List2ArrayConverter;
import core.plotting.chart_plotting.ChartSaver;
import core.plotting.plotting_2d.StairDataGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;
import java.awt.*;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecorderPlotter {

    public static final String X_LABEL = "Trial";

    Recorder recorder;
    private final String filePath;
    private final String fileNameAddOn;
    private final ProjectPropertiesReader reader;

    public static RecorderPlotter of(Recorder recorder,
                                     String filePath,
                                     String fileNameAddOn,
                                     ProjectPropertiesReader reader) {
        return new RecorderPlotter(recorder, filePath, fileNameAddOn, reader);
    }

    public void plotAndSave(List<MeasuresCombLPEnum> measures) {
        Preconditions.checkArgument(!recorder.isEmpty(), "No training progress data to plot");
        measures.forEach(measure -> showAndSavePlot(measure));
    }

    private void showAndSavePlot(MeasuresCombLPEnum measure) {
        var yData = List2ArrayConverter.convertListToDoubleArr(recorder.trajectory(measure));
        var xData = ArrayCreator.createArrayInRange(0, 1, yData.length - 1);
        var xyDataStair = StairDataGenerator.generateWithEndStep(Pair.create(xData, yData));
        var chart = new XYChartBuilder()
                .width(reader.xyChartWidth2Col()).height((int) (reader.xyChartHeight()))
                .title(" ").xAxisTitle(X_LABEL).yAxisTitle(measure.description).build();
        chart.getStyler()
                .setLegendVisible(false)
                .setChartBackgroundColor(Color.white)
                .setSeriesColors(new Color[]{Color.black});
        chart.addSeries("Stairs", xyDataStair.getFirst(), xyDataStair.getSecond()).setMarker(SeriesMarkers.NONE);
        ChartSaver.saveAndShowXYChart(chart, PathAndFile.ofPng(filePath, measure + fileNameAddOn));
    }

}
