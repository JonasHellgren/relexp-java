package core.animation;

import org.jetbrains.annotations.NotNull;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartFactory {

    public static @NotNull JFreeChart emptyHeatMapChart(String title) {
        return emptyHeatMapChart(title, 1, 1);
    }

    public static @NotNull JFreeChart emptyHeatMapChart(String title, int nRows, int nCols) {
        return HeatMapCharts.fromGrid(new double[nCols][nRows], title);
    }

    public static JFreeChart emptyLineChart(String title) {
        var chart = org.jfree.chart.ChartFactory.createXYLineChart(
                title,
                "X",
                "Y",
                new XYSeriesCollection()
        );
        return chart;
    }
}
