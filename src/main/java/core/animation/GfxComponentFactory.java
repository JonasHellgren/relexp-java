package core.animation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GfxComponentFactory {

    AnimationSettings settings;
    @Getter
    List<JFreeChart> lineCharts;
    List<ChartPanel> linePanels;
    @Getter
    List<JFreeChart> heatMapCharts;
    List<ChartPanel> heatMapPanels;
    List<JScrollPane> tablePanels;
    @Getter
    List<DefaultTableModel> tableModels;

    public static GfxComponentFactory of(AnimationSettings settings) {
        return new GfxComponentFactory(
                settings,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());
    }

    public void addLineChart(
            String title, String xlabel, Pair<Integer, Integer> xRange, String ylabel, Pair<Integer, Integer> yRange) {
        var chart = ChartFactory.emptyLineChart(title);
        CharAndPanelSizeSetter.setXaxisRange(chart, xlabel, xRange, getLabelFont(settings.fontsizeAxis()));
        CharAndPanelSizeSetter.setYaxisRange(chart, ylabel, yRange, getLabelFont(settings.fontsizeAxis()));
        lineCharts.add(chart);
        var panel = new ChartPanel(chart);
        CharAndPanelSizeSetter.setSizePanel(panel, settings.panelWidth(), settings.panelHeight());
        linePanels.add(panel);
    }


    public void addHeatMap(String title) {
        var chart = ChartFactory.emptyHeatMapChart(title);
        var panel = new ChartPanel(chart);
        CharAndPanelSizeSetter.setSizePanel(panel, settings.panelWidth(), settings.panelHeight());
        heatMapPanels.add(panel);
        heatMapCharts.add(chart);
    }

    public void addTable(int nColumns, boolean showGrid) {
        String[] columnNames = ArrayUtils.EMPTY_STRING_ARRAY;
        for (int i = 0; i < nColumns; i++) {
            columnNames = ArrayUtils.add(columnNames, "");
        }
        addTable(columnNames, showGrid);
    }

    public void addTable(String[] columnNames, boolean showGrid) {
        var tm = TableModelFactory.of(columnNames);
        var panel = getTablePanel(tm, showGrid);
        CharAndPanelSizeSetter.setTable(panel, settings.tableWidth(), settings.tableHeight());
        tableModels.add(tm);
        tablePanels.add(panel);
    }

    public JPanel getRootPanel() {
        JPanel rootPanel = JPanelUtil.createYStacked(settings.margin());
        JPanelUtil.addPanelsToRoot(rootPanel, linePanels, heatMapPanels, tablePanels, settings.order());
        return rootPanel;
    }

    private static JScrollPane getTablePanel(DefaultTableModel tm, boolean showGrid) {
        var table1 = new JTable(tm);
        table1.setShowGrid(showGrid);
        return new JScrollPane(table1);
    }


    @NotNull
    private static Font getLabelFont(int size) {
        return new Font("Arial", Font.BOLD, size);
    }



}
