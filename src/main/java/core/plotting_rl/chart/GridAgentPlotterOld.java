package core.plotting_rl.chart;

import core.foundation.config.PlotConfig;
import core.gridrl.AgentGridI;
import core.gridrl.InformerGridParamsI;
import core.gridrl.TrainerGridDependencies;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.knowm.xchart.HeatMapChart;
import static chapters.ch4.plotting.GridPlotHelper.*;
import static core.plotting.chart_plotting.ChartSaverAndPlotter.showAndSaveHeatMapFolderSafe;
import static core.plotting.chart_plotting.ChartSaverAndPlotter.showAndSaveHeatMapFolderTempDiff;


/**
 * A utility class for plotting heat maps of agent data in a grid environment.
 */
@AllArgsConstructor
@Getter
public class GridAgentPlotterOld {

    private final EnvironmentGridI environment;
    private final AgentGridI agent;
    private final String fileNameAddOn;
    private final int nofDigits;
    PlotConfig plotCfg;

    public static GridAgentPlotterOld of(TrainerGridDependencies dependencies,
                                         String fileNameAddO, int nofDigits, PlotConfig plotCfg) {
        return GridAgentPlotterOld.of(
                dependencies.environment(),
                dependencies.agent(),
                fileNameAddO,
                nofDigits,
                plotCfg);
    }

    public static GridAgentPlotterOld of(EnvironmentGridI environment,
                                         AgentGridI agent,
                                         String fileNameAddO,
                                         PlotConfig plotCfg) {
        return new GridAgentPlotterOld(environment, agent, fileNameAddO, 1,plotCfg);
    }

    public static GridAgentPlotterOld of(EnvironmentGridI environment,
                                         AgentGridI agent,
                                         String fileNameAddO, int nofDigits, PlotConfig plotCfg) {
        return new GridAgentPlotterOld(environment, agent, fileNameAddO, nofDigits,plotCfg);
    }

    @SneakyThrows
    public void plotAndSaveStateValuesInFolderTempDiff() {
        HeatMapChart chart = getValueChart();
        showAndSaveHeatMapFolderTempDiff(chart, "values", fileNameAddOn);
    }

    @SneakyThrows
    public void plotAndSavePolicyInFolderTempDiff() {
        HeatMapChart chart = getPolicyChart();
        showAndSaveHeatMapFolderTempDiff(chart, "policy", fileNameAddOn);
    }

    @SneakyThrows
    public void plotStateValuesInFolderSafe() {
        HeatMapChart chart = getValueChart();
        showAndSaveHeatMapFolderSafe(chart, "values", fileNameAddOn);
    }

    @SneakyThrows
    public void plotPolicyInFolderSafe() {
        HeatMapChart chart = getPolicyChart();
       // ChartSaverAndPlotter.showAndSaveHeatMap(chart, pathAndFile);
        showAndSaveHeatMapFolderSafe(chart, "policy", fileNameAddOn);
    }


    @SneakyThrows
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

}

