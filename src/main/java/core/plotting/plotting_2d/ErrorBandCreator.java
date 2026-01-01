package core.plotting.plotting_2d;

import com.google.common.base.Preconditions;
import core.foundation.util.cond.Conditionals;
import core.plotting.base.shared.PlotSettings;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static core.foundation.util.cond.Conditionals.executeIfTrue;

@AllArgsConstructor
public class ErrorBandCreator {

    record ErrorBand(
            String title,
            double[] xData,
            double[] yData,
            double[] errData,
            Color lineColor) {
    }


    private final PlotSettings settings;
    private final List<ErrorBand> errorBands;

    public static ErrorBandCreator newDefault() {
        return newOfSettings(PlotSettings.ofDefaults());
    }

    public static ErrorBandCreator newOfSettings(PlotSettings settings) {
        return new ErrorBandCreator(settings, new ArrayList<>());
    }

    public void addErrorBand(String title, double[] xData, double[] yData, double[] errData) {
        addErrorBand(title, xData, yData, errData, settings.lineColorBand());
    }

    public void addErrorBand(String title, double[] xData, double[] yData, double[] errData, Color lineColor) {
        errorBands.add(new ErrorBand(title, xData, yData, errData, lineColor));
    }

    public JFrame createFrame() {
        var chart = createChart();
        return createFrame(chart);
    }

    @SneakyThrows
    public void saveAsPicture(String path) {
        var chart = createChart();
        ChartUtils.saveChartAsPNG(new File(path), chart, settings.width(), settings.height());
    }

    public JFreeChart createChart() {
        Preconditions.checkArgument(!errorBands.isEmpty(), "No error bands defined");
        var dataset = createDataset();
        var chart = createChart(dataset);
        setStyles(chart);
        return chart;
    }

    JFrame createFrame(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame("Error band plot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(settings.width(), settings.height()));
        frame.pack();
        return frame;
    }

    private void setStyles(JFreeChart chart) {
        chart.getTitle().setFont(settings.axisTitleFont());
        Conditionals.executeIfTrue(settings.showLegend(), () ->
        chart.getLegend().setItemFont(settings.legendTextFont()));
        XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setLabelFont(settings.axisTitleFont());
        plot.getRangeAxis().setLabelFont(settings.axisTitleFont());
        plot.setBackgroundPaint(settings.bandPlotBackGroundColor());
        plot.setRangeGridlinePaint(settings.gridLineColor());
        plot.setDomainGridlinePaint(settings.gridLineColor());
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        xAxis.setTickLabelFont(settings.axisTicksFont());
        yAxis.setTickLabelFont(settings.axisTicksFont());
        executeIfTrue(!settings.showAxisTicks(), () -> disableTickMarks(plot));
        //executeIfTrue(!settings.showTitle(), () -> chart.setTitle(""));
        DeviationRenderer renderer = new DeviationRenderer(true, true);
        var markers=markers();
        for (int i = 0; i < errorBands.size(); i++) {
            renderer.setSeriesFillPaint(i, settings.bandFillColor()); // Set color for error band
            renderer.setSeriesPaint(i, errorBands.get(i).lineColor); // Line color
            renderer.setSeriesShapesVisible(i, settings.showMarker());
            renderer.setSeriesShape(i, markers.get(i));
            renderer.setSeriesStroke(i, new BasicStroke(settings.lineWidth()));
        }
        plot.setRenderer(renderer);
    }

    private List<RectangularShape> markers() {

        List<Function<Double, RectangularShape>> shapeFactories = List.of(
                s -> new Ellipse2D.Double(-s / 2, -s / 2, s, s),
                s -> new Rectangle2D.Double(-s / 2, -s / 2, s, s),
                s -> new Ellipse2D.Double(-s / 3, -s / 2, s / 2, s*2)
                // Add more shapes here if you want
        );
        List<RectangularShape> markers = new ArrayList<>();
        double size = settings.markeSize();
        for (int i = 0; i < errorBands.size(); i++) {
            // Pick a shape factory based on index cycling through list
            Function<Double, RectangularShape> factory = shapeFactories.get(i % shapeFactories.size());
            markers.add(factory.apply(size));
        }
        return markers;
    }

    private void disableTickMarks(XYPlot plot) {
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        xAxis.setTickMarksVisible(false); // Disable tick marks
        xAxis.setTickLabelsVisible(false); // Disable tick labels
        yAxis.setTickMarksVisible(false); // Disable tick marks
        yAxis.setTickLabelsVisible(false); // Disable tick labels
    }

    private JFreeChart createChart(YIntervalSeriesCollection dataset) {
        return ChartFactory.createScatterPlot(
                settings.title(), settings.xAxisLabel(), settings.yAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                settings.showLegend(), true, false
        );
    }

    private YIntervalSeriesCollection createDataset() {
        var dataset = new YIntervalSeriesCollection();
        for (ErrorBand band : errorBands) {
            YIntervalSeries series = new YIntervalSeries(band.title);
            for (int i = 0; i < band.xData.length; i++) {
                double x = band.xData[i];
                double y = band.yData[i];
                double yLow = y - band.errData[i];
                double yHigh = y + band.errData[i];
                series.add(x, y, yLow, yHigh);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }


}
