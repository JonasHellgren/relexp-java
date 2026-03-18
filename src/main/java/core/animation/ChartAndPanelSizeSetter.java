package core.animation;

import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import javax.swing.*;
import java.awt.*;

@UtilityClass
public class ChartAndPanelSizeSetter {


    public static void setSizePanel(ChartPanel p, int panelWidth, int panelHeight) {
        p.setPreferredSize(new Dimension(panelWidth, panelHeight));
        p.setMaximumSize(new Dimension(panelWidth, panelHeight));
    }

    public static void setTable(JScrollPane p, int tableWidth, int tableHeight) {
        p.setPreferredSize(new Dimension(tableWidth,tableHeight));
        p.setMaximumSize(new Dimension(tableWidth, tableHeight));
    }

    public static void setYaxisRange(JFreeChart chart, String label, Pair<Integer, Integer> range, Font font) {
        var axis = new NumberAxis(label);
        chart.getXYPlot().setRangeAxis(axis);
        axis.setRange(range.getFirst(), range.getSecond());
        axis.setLabelFont(font);
    }

    public static void setXaxisRange(JFreeChart chart, String label, Pair<Integer, Integer> range, Font font) {
        var axis = new NumberAxis(label);
        chart.getXYPlot().setDomainAxis(axis);
        axis.setRange(range.getFirst(), range.getSecond());
        axis.setLabelFont(font);
    }

}
