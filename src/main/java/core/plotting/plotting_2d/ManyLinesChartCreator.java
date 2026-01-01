package core.plotting.plotting_2d;

import com.google.common.base.Preconditions;
import core.foundation.util.cond.Conditionals;
import core.foundation.util.math.MyMathUtils;
import core.foundation.util.math.MyMatrixListUtils;
import core.plotting.base.shared.PlotSettings;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static core.plotting.base.shared.FormattedAsString.getFormattedAsString;

@AllArgsConstructor
@Log
public class ManyLinesChartCreator {
    private final PlotSettings settings;
    List<Double> xList;
    List<List<Double>> yData;
    List<String> names;

    public static ManyLinesChartCreator defaultSettings(List<Double> xData) {
        return ManyLinesChartCreator.of(PlotSettings.ofDefaults(), xData);
    }

    public static ManyLinesChartCreator of(PlotSettings settings, List<Double> xData) {
        Preconditions.checkArgument(xData.size() > 1, "xData must have at least 2 elements");
        Preconditions.checkArgument(xData.get(1) > xData.get(0), "xData must be increasing");
        Conditionals.executeIfTrue(settings.isDefinedSpaceBetweenXTicks(), () ->
                Preconditions.checkArgument(xData.get(1) % 1 == 0,
                "xData must have even elements, elem=" + xData.get(1)));
        return new ManyLinesChartCreator(settings, xData, new ArrayList<>(), new ArrayList<>());
    }

    public void addLine(String name, List<Double> yList) {
        Preconditions.checkArgument(yList.size() == xList.size(), "yList and xData should have same size." +
                " Size y list = " + yList.size() + ", size x list = " + xList.size());
        this.yData.add(yList);
        this.names.add(name);
    }

    public void clear() {
        yData.clear();
        names.clear();
    }

    public XYChart create() {
        var s = settings;
        var chart = new XYChartBuilder()
                .title(s.title())
                .xAxisTitle(s.xAxisLabel()).yAxisTitle(s.yAxisLabel())
                .width(s.width()).height(s.height()).build();
        var styler = chart.getStyler();
        styler.setYAxisMin(MyMatrixListUtils.findMin(yData));
        styler.setYAxisMax(MyMatrixListUtils.findMax(yData));
        styler.setChartTitleVisible(true);
        styler.setLegendVisible(s.showLegend()).setLegendPosition(s.legendPosition());
        styler.setLegendFont(s.legendTextFont());
        styler.setAxisTicksVisible(s.showAxisTicks());
        styler.setPlotGridLinesVisible(s.showGridLines());
        styler.setAxisTitleFont(s.axisTitleFont());
        styler.setAxisTickLabelsFont(s.axisTicksFont());
        styler.setChartTitleFont(s.axisTitleFont());
        styler.setChartBackgroundColor(Color.WHITE);
        setAxisTicksFormatting(styler);
        Conditionals.executeIfTrue(s.colorRangeSeries() != null, () ->
                styler.setSeriesColors(s.colorRangeSeries()));
        for (String name : names) {
            int i = names.indexOf(name);
            var series = chart.addSeries(name, xList, yData.get(i));
            Conditionals.executeIfFalse(s.showMarker(), () -> series.setMarker(SeriesMarkers.NONE));
        }
        return chart;
    }

    private void setAxisTicksFormatting(XYStyler styler) {
        if (settings.isDefinedSpaceBetweenXTicks()) {
            styler.setxAxisTickLabelsFormattingFunction(value ->
                    (MyMathUtils.isZero(value % settings.spaceBetweenXTicks())
                            ? getFormattedAsString(value, settings.axisTicksDecimalFormat())
                            : ""));
        } else {
            styler.setxAxisTickLabelsFormattingFunction(value ->
                    getFormattedAsString(value, settings.axisTicksDecimalFormat()));
        }
        styler.setyAxisTickLabelsFormattingFunction(value ->
                getFormattedAsString(value, settings.axisTicksDecimalFormat()));
    }

}
