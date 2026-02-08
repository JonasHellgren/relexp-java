package core.animation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;

public final class ChartFactoryUtil {

    private ChartFactoryUtil() {}

    public static JFreeChart createEmptyXYChart(
            String title, String xLabel, String yLabel
    ) {
        return ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                new XYSeriesCollection()
        );
    }

    public static XYPlot getPlot(JFreeChart chart) {
        return chart.getXYPlot();
    }
}
