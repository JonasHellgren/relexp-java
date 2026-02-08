package core.animation;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.xy.DefaultXYZDataset;

public final class HeatMapCharts {
    private HeatMapCharts() {}

    public static JFreeChart fromGrid(double[][] grid, String title) {
        DefaultXYZDataset ds = GridToXYZDataset.from(grid, "heat");
        MinMax mm = MinMax.of(grid);

        NumberAxis xAxis = new NumberAxis("x");
        NumberAxis yAxis = new NumberAxis("y");
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        XYBlockRenderer r = new XYBlockRenderer();
        r.setBlockWidth(1.0);   // cellbredd: 1 “index-enhet”
        r.setBlockHeight(1.0);  // cellhöjd
        r.setPaintScale(PaintScales.grayscale(mm.min, mm.max, 200));

        XYPlot plot = new XYPlot(ds, xAxis, yAxis, r);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);

        return new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, false);
    }
}
