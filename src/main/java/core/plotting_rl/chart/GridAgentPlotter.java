package core.plotting_rl.chart;

import core.foundation.config.PlotConfig;
import core.foundation.config.PathAndFile;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.gridrl.*;
import core.plotting.chart_plotting.ChartSaver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.SwingWrapper;

import static chapters.ch4.plotting.GridPlotHelper.*;


/**
 * A utility class for plotting heat maps of agent data in a grid environment.
 */
@AllArgsConstructor
@Getter
public class GridAgentPlotter {

    private final EnvironmentGridI environment;
    private final AgentGridI agent;
    private final int nofDigits;
    PlotConfig plotCfg;
    private final String path;

    public static GridAgentPlotter of(TrainerGridDependencies dependencies,
                                      int nofDigits, PlotConfig plotCfg,String path) {
        return GridAgentPlotter.of(
                dependencies.environment(),
                dependencies.agent(),
                nofDigits,
                plotCfg,
                path);
    }


    public static GridAgentPlotter of(EnvironmentGridI environment,
                                      AgentGridI agent,
                                      int nofDigits, PlotConfig plotCfg,
                                      String path) {
        return new GridAgentPlotter(environment, agent, nofDigits,plotCfg,path);
    }

    public void saveAndPlotStateValues(String fileName) {
        var chart = getValueChart();
        showAndSaveHeatMap(chart,fileName);
    }

    public void saveAndPlotPolicy(String fileName) {
        var chart = getPolicyChart();
        showAndSaveHeatMap(chart,fileName);
    }


    private HeatMapChart getPolicyChart() {
        int nCols = getNofCols(environment);
        int nRows = getNofRows(environment);
        var data = getPolicyData(nRows, nCols);
        return getStringTextChart(data, nCols, nRows,plotCfg);
    }

    private static boolean isNoDecisionCell(InformerGridParamsI informer, StateGrid state) {
        return informer.isTerminal(state) || informer.isWall(state);
    }

    private HeatMapChart getValueChart() {
        int nCols = getNofCols(environment);
        int nRows = getNofRows(environment);
        String[][] valueData = getValueData(nRows, nCols);
        return getStringTextChart(valueData, nCols, nRows,plotCfg);
    }

    private String[][] getValueData(int nRows, int nCols) {
        return getStringData(nRows, nCols, true);
    }

    private String[][] getPolicyData(int nRows, int nCols) {
        return getStringData(nRows, nCols, false);
    }

    private String[][] getStringData(int nRows, int nCols, boolean isShowValue) {
        String[][] valueData = new String[nRows][nCols];
        var informer = environment.informer();
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                var state = StateGrid.of(x, y);
                var stringToMaybeShow = isShowValue
                        ? getValueInStateAsString(state)
                        : getActionAsString(state);
                valueData[y][x] = isNoDecisionCell(informer, state)
                        ? "."
                        : stringToMaybeShow;
            }
        }
        return valueData;
    }

    public String getValueInStateAsString(StateGrid state) {
        return NumberFormatterUtil.getRoundedNumberAsString(
                agent.readValue(state), nofDigits);
    }

    public String getActionAsString(StateGrid state) {
        return agent.chooseActionNoExploration(state).toString();
    }

    private  void showAndSaveHeatMap(HeatMapChart chart,String fileName) {
        PathAndFile pathAndFile = PathAndFile.ofPng(path, fileName);
        ChartSaver.saveHeatMapChart(chart, pathAndFile);
        new SwingWrapper<>(chart).displayChart();
    }


}

