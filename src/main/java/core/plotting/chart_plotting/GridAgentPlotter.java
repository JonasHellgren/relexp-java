package core.plotting.chart_plotting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.knowm.xchart.HeatMapChart;

import java.awt.*;
import java.io.IOException;


/**
 * A utility class for plotting heat maps of agent data in a grid environment.
 */
@AllArgsConstructor
@Getter
public class GridAgentPlotter {
/*

    private final Font ARROW_TEXT_FONT = new Font("Arial", Font.PLAIN, 22);
    private final Font VALUE_TEXT_FONT = new Font("Arial", Font.PLAIN, 12);
    private final EnvironmentGridI environment;
    private final AgentGridI agent;
    private final String fileNameAddOn;
    private final int nofDigits;



    public static GridAgentPlotter of(TrainerGridDependencies dependencies,
                                      String fileNameAddO, int nofDigits) {
        return GridAgentPlotter.of(
                dependencies.environment(),
                dependencies.agent(),
                fileNameAddO,
                nofDigits);
    }

    public static GridAgentPlotter of(EnvironmentGridI environment,
                                      AgentGridI agent,
                                      String fileNameAddO) {
        return new GridAgentPlotter(environment, agent, fileNameAddO, 1);
    }

    public static GridAgentPlotter of(EnvironmentGridI environment,
                                      AgentGridI agent,
                                      String fileNameAddO, int nofDigits) {
        return new GridAgentPlotter(environment, agent, fileNameAddO, nofDigits);
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
        showAndSaveHeatMapFolderSafe(chart, "policy", fileNameAddOn);
    }


    @SneakyThrows
    private HeatMapChart getPolicyChart() {
        int nCols = getNofCols(environment);
        int nRows = getNofRows(environment);
        var data = getPolicyData(nRows, nCols);
        return getStringTextChart(data, nCols, nRows, ARROW_TEXT_FONT);
    }

    private static boolean isNoDecisionCell(EnvironmentGridParametersI parameters, StateGrid state) {
        return parameters.isTerminal(state) || parameters.isWall(state);
    }

    private HeatMapChart getValueChart() throws IOException {
        int nCols = getNofCols(environment);
        int nRows = getNofRows(environment);
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
        var parameters = environment.getParameters();
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                var state = StateGrid.of(x, y);
                var stringToMaybeShow = isShowValue
                        ? getValueInStateAsString(state)
                        : getActionAsString(state);
                valueData[y][x] = isNoDecisionCell(parameters, state)
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

*/

}

