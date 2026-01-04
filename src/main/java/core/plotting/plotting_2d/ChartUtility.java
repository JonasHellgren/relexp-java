package core.plotting.plotting_2d;

import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class ChartUtility {


    public static void showChartWithoutMarkers(XYChart chart) {
        chart.getSeriesMap().values().forEach(s -> s.setMarker(SeriesMarkers.NONE));
        new SwingWrapper<>(chart).displayChart();
    }

    public static void showCharts(List<XYChart> charts) {
        new SwingWrapper<>(charts).displayChartMatrix();
    }

    public static void removeMarkerInCharts(List<XYChart> charts) {
        charts.forEach(c -> c.getSeriesMap().values().forEach(s -> s.setMarker(SeriesMarkers.NONE)));
    }

    public static void setColorInCharts(List<XYChart> charts) {
        charts.forEach(c -> c.getStyler().setChartBackgroundColor(Color.WHITE));
    }

    public static void setBorderInCharts(List<XYChart> charts) {
        charts.forEach(c -> c.getStyler().setPlotBorderVisible(false)); // Border
    }

    public static void setStyleLegend(XYChart chart,
                                      Pair<Double, Double> yMinMax,
                                      Styler.LegendPosition legendPosition,
                                      Font font) {
        setStyle(chart, yMinMax, legendPosition, font, true);
    }


    public static void setStyleNoLegend(XYChart chart,
                                        Pair<Double, Double> yMinMax,
                                        Font font) {
        setStyle(chart, yMinMax, Styler.LegendPosition.InsideNW, font, false);
    }


    private static void setStyle(XYChart chart,
                                Pair<Double, Double> yMinMax,
                                Styler.LegendPosition legendPosition,
                                Font font,
                                boolean isLegendVisible) {
        XYStyler styler = chart.getStyler();
        styler.setLegendVisible(isLegendVisible);
        styler.setPlotGridLinesVisible(true);
        styler.setLegendPosition(legendPosition);
        styler.setYAxisMin(yMinMax.getFirst());
        styler.setYAxisMax(yMinMax.getSecond());
        styler.setXAxisDecimalPattern("#.#");
        styler.setAxisTitleFont(font);
        styler.setLegendFont(font);

    }

    /**
     * round, floor or ceil make difference
     */

    public static void reduceXAxisTicksClutter(XYChart chart, int xStep, String elseStr) {
        Function<Double, String> tickLabelsFormatter = value -> {
            int intValue = (int) Math.floor(value);
            if (intValue % xStep == 0) {
                return String.valueOf(intValue);
            } else {
                return elseStr;
            }
        };
       chart.setCustomXAxisTickLabelsFormatter(tickLabelsFormatter);
    }

}
