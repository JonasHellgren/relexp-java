package chapters.ch4.plotting;

import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.knowm.xchart.HeatMapChart;

import java.awt.*;
import java.io.IOException;

import static chapters.ch4.domain.helper.GridPlotHelper.*;
import static core.plotting.chart_plotting.ChartSaverAndPlotter.showAndSaveHeatMapFolderSafe;
import static core.plotting.chart_plotting.ChartSaverAndPlotter.showAndSaveHeatMapFolderTempDiff;

/**
 * A utility class for plotting heat maps of agent data in a grid environment.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GridAgentPlottercH4VECK {

    private final Font ARROW_TEXT_FONT = new Font("Arial", Font.PLAIN, 22);
    private final Font VALUE_TEXT_FONT = new Font("Arial", Font.PLAIN, 12);
    private final TrainerGridDependencies dependencies;
    private final String fileNameAddOn;
    private final int nofDigits;

    public static GridAgentPlottercH4VECK of(TrainerGridDependencies dependencies,
                                             String fileNameAddO) {
        return new GridAgentPlottercH4VECK(dependencies, fileNameAddO, 1);
    }

    public static GridAgentPlottercH4VECK of(TrainerGridDependencies dependencies,
                                             String fileNameAddO,
                                             int nofDigits) {
        return new GridAgentPlottercH4VECK(dependencies,  fileNameAddO, nofDigits);
    }

    @SneakyThrows
    public void plotStateValuesTd() {
        HeatMapChart chart = getValueChart();
        showAndSaveHeatMapFolderTempDiff(chart, "values", fileNameAddOn);
    }

    @SneakyThrows
    public void plotPolicyTd() {
        HeatMapChart chart = getPolicyChart();
        showAndSaveHeatMapFolderTempDiff(chart, "policy", fileNameAddOn);
    }


    @SneakyThrows
    public void plotStateValuesSafe() {
        HeatMapChart chart = getValueChart();
        showAndSaveHeatMapFolderSafe(chart, "values", fileNameAddOn);
    }

    @SneakyThrows
    public void plotPolicySafe() {
        HeatMapChart chart = getPolicyChart();
        showAndSaveHeatMapFolderSafe(chart, "policy", fileNameAddOn);
    }


    @SneakyThrows
    private HeatMapChart getPolicyChart() {
        int nCols = getNofCols(dependencies.environment());
        int nRows = getNofRows(dependencies.environment());
        var data =  getPolicyData(nRows, nCols);
        return getStringTextChart(data, nCols, nRows, ARROW_TEXT_FONT);
    }

    private static boolean isNoDecisionCell(EnvironmentGridParametersI parameters, StateGrid state) {
        return parameters.isTerminal(state) || parameters.isWall(state);
    }

    private HeatMapChart getValueChart() throws IOException {
        int nCols = getNofCols(dependencies.environment());
        int nRows = getNofRows(dependencies.environment());
        String[][] valueData = getValueData(nRows, nCols);
        return getStringTextChart(valueData, nCols, nRows, VALUE_TEXT_FONT);
    }

    private String[][] getValueData(int nRows, int nCols) {
        return getStringData(nRows, nCols, true);
    }

    private String[][] getPolicyData(int nRows, int nCols) {
        return getStringData(nRows, nCols, false);
    }

    private String[][] getStringData(int nRows, int nCols, boolean isShowValue) {
        String[][] valueData = new String[nRows][nCols];
        var parameters = dependencies.environment().getParameters();
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                var state = StateGrid.of(x, y);
                var stringToMaybeShow=isShowValue
                        ? getValueInStateAsString(state)
                        : getActionAsString(state);
                valueData[y][x] = isNoDecisionCell(parameters, state)
                        ? "."
                        : stringToMaybeShow;
            }
        }
        return valueData;
    }

    private String getValueInStateAsString(StateGrid state) {
        return NumberFormatterUtil.getRoundedNumberAsString(
                dependencies.agent().readValue(state), nofDigits);
    }

    private String getActionAsString(StateGrid state) {
        return dependencies.agent().chooseActionNoExploration(state).toString();
    }


}

