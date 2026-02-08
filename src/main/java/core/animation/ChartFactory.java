package core.animation;

import org.jetbrains.annotations.NotNull;
import org.jfree.chart.JFreeChart;

public class ChartFactory {

    public static @NotNull JFreeChart emptyChart(String title) {
        return emptyChart(title, 1, 1);
    }

    public static @NotNull JFreeChart emptyChart(String title,int nRows, int nCols) {
        return HeatMapCharts.fromGrid(new double[nCols][nRows], title);
    }
}
