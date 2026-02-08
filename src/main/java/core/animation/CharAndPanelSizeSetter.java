package core.animation;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import javax.swing.*;
import java.awt.*;

public class CharAndPanelSizeSetter {


    public static void setSize(ChartPanel p, int panelWidth, int panelHeight) {
        p.setPreferredSize(new Dimension(panelWidth, panelHeight));
        p.setMaximumSize(new Dimension(panelWidth, panelHeight));
    }

    public static void setSize(JScrollPane p, int tableWidth, int tableHeight) {
        p.setPreferredSize(new Dimension(tableWidth,tableHeight));
        p.setMaximumSize(new Dimension(tableWidth, tableHeight));
    }

    public static void setYaxisRange(JFreeChart chart, String label, int miny, int maxy) {
        var axis = new NumberAxis(label);
        chart.getXYPlot().setRangeAxis(axis);
        axis.setRange(miny, maxy);
    }

    public static void setXaxisRange(JFreeChart chart, String label, int minx, int maxx) {
        var axis = new NumberAxis(label);
        chart.getXYPlot().setDomainAxis(axis);
        axis.setRange(minx, maxx);
    }

}
