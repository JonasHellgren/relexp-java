package core.animation;

import org.jetbrains.annotations.NotNull;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.util.List;

public class JPanelUtil {

    public static @NotNull JPanel createYStacked(int margin) {
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(margin,margin,margin,margin));
        return rootPanel;
    }


    record Panels(JPanel rootPanel,
                  List<ChartPanel> linePanels,
                  List<ChartPanel> gridPanels,
                  List<JScrollPane> tablePanels) {
    }

    public static @NotNull JPanel addPanelsToRoot(JPanel rootPanel,
                                                  List<ChartPanel> linePanels,
                                                  List<ChartPanel> gridPanels,
                                                  List<JScrollPane> tablePanels) {

        return addPanelsToRoot(rootPanel, linePanels, gridPanels, tablePanels, List.of(Step.LINE, Step.HEATMAP, Step.TABLE));
                                                  }

    /**
     * order determines placement of panels and tables
     */

    public static @NotNull JPanel addPanelsToRoot(JPanel rootPanel,
                                                  List<ChartPanel> linePanels,
                                                  List<ChartPanel> gridPanels,
                                                  List<JScrollPane> tablePanels,
                                                  List<Step> order) {

        var panels = new Panels(rootPanel,linePanels, gridPanels, tablePanels);
        order.forEach(step -> execute(step, panels));
        return rootPanel;
    }

    public  static void execute(Step step, Panels panels) {
        switch (step) {
            case LINE -> panels.linePanels.forEach(p -> panels.rootPanel.add(p));
            case HEATMAP -> panels.gridPanels.forEach(p -> panels.rootPanel.add(p));
            case TABLE -> panels.tablePanels.forEach(p -> panels.rootPanel.add(p));
        }
    }

}