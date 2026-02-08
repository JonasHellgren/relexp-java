package core.animation;

import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.data.xy.DefaultXYZDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Renderer {
    public static final String SERIES_NAME = "DUMMY";
    private List<JFreeChart> lineCharts;
    private List<JFreeChart> heatCharts;
    private List<DefaultTableModel> tableModels;

    public static Renderer of(List<JFreeChart> lineCharts,
                           List<JFreeChart> heatCharts,
                           List<DefaultTableModel> tableModels) {
         return new Renderer(lineCharts, heatCharts, tableModels);
    }

    @Subscribe
    public void render(GraphicsDto dto) {

        SwingUtilities.invokeLater(() -> {
            for (JFreeChart chart : lineCharts) {
                int i = lineCharts.indexOf(chart);
                deleteOldLines(chart);
                addNewLines(chart, dto.getLines(i));
                var color=dto.isFail()?Color.RED:Color.WHITE;
                chart.setBackgroundPaint(color);
            }

            for (JFreeChart chart : heatCharts) {
                int i = heatCharts.indexOf(chart);
                i=Math.min(i, dto.getGrid(i).length-1);
                System.out.println("i = " + i);
                var dataset = new DefaultXYZDataset();
                dataset.removeSeries(SERIES_NAME);
                dataset.addSeries(SERIES_NAME, dto.getGrid(i));

                System.out.println("dto.getGrid(i) = " + Arrays.deepToString(dto.getGrid(i)));

                chart.getXYPlot().setDataset(dataset);
            }
            for (DefaultTableModel tableModel : tableModels) {
                int i = tableModels.indexOf(tableModel);
                Object[][] td = dto.getTable(i);
                roundTableData(td);
                var columnNames = getColumnNames(tableModel);
                tableModel.setDataVector(td, columnNames);
            }
        });
    }

    private String[] getColumnNames(DefaultTableModel tableModel) {
        int numColumns = tableModel.getColumnCount();
        String[] columnNames = new String[numColumns];
        for (int i = 0; i < numColumns; i++) {
            columnNames[i] = tableModel.getColumnName(i);
        }
        return columnNames;
    }

    private void roundTableData(Object[][] tableData) {
        for (int row = 0; row < tableData.length; row++) {
            for (int col = 0; col < tableData[row].length; col++) {
                if (tableData[row][col] instanceof Number) {
                    Number number = (Number) tableData[row][col];
                    tableData[row][col] = roundNumber(number);
                }
            }
        }
    }

    private Object roundNumber(Number number) {
        double value = number.doubleValue();
        return Math.round(value * 100.0) / 100.0;
    }

    private void addNewLines(JFreeChart chart,List<LineSegment> lines) {
        var plot=chart.getXYPlot();
        for (var l : lines) {
            var ann = getAnnotation(l);
            plot.addAnnotation(ann);
        }
    }

    private void deleteOldLines(JFreeChart chart) {
        var plot = chart.getXYPlot();
        for (Object annotation : plot.getAnnotations()) {
            if (annotation instanceof XYLineAnnotation) {
                plot.removeAnnotation((XYAnnotation) annotation);
            }
        }
    }

    private static XYLineAnnotation getAnnotation(LineSegment l) {
        var stroke = new BasicStroke(l.thickness(), l.endRounding(), l.joining());
        return new XYLineAnnotation(
                l.x1(), l.y1(),
                l.x2(), l.y2(),
                stroke, l.color()
        );
    }



}
