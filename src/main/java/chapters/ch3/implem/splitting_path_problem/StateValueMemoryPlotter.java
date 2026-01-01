package chapters.ch3.implem.splitting_path_problem;

import core.foundation.config.PathAndFile;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.gridrl.StateGrid;
import core.plotting.chart_plotting.ChartSaver;
import core.plotting.chart_plotting.StringTextChartFactory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.SwingWrapper;
import java.awt.*;
import java.io.IOException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StateValueMemoryPlotter {

    public static final int NOF_DIGITS = 2;
    private final Font VALUE_TEXT_FONT = new Font("Arial", Font.PLAIN, 12);

    private final String filePath;
    private final String fileNameAddOn;
    StateValueMemoryGrid memory;
    EnvironmentParametersSplitting parameters;


    @SneakyThrows
    public void plotAndSaveStateValues() {
        HeatMapChart chart = getValueChart();
        ChartSaver.saveHeatMapChart(chart, PathAndFile.ofPng(filePath, "values" + fileNameAddOn));
        new SwingWrapper<>(chart).displayChart();
    }


    private HeatMapChart getValueChart() throws IOException {
        int nCols = parameters.getPosXMinMax().getSecond()+1;
        int nRows = parameters.getPosYMinMax().getSecond()+1;
        String[][] valueData = getStringData(nRows, nCols);
        return StringTextChartFactory.produce(valueData, nCols, nRows,VALUE_TEXT_FONT);
    }

    private String[][] getStringData(int nRows, int nCols) {
        String[][] valueData = new String[nRows][nCols];
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                var state = StateGrid.of(x, y);
                boolean isWall=parameters.getWallStates().contains(state);
                boolean isTerminal=parameters.isTerminal(state);
                valueData[y][x] = isWall || isTerminal ? "."  : getNumberAsString(state);
            }
        }
        return valueData;
    }

    private String getNumberAsString(StateGrid state) {
        return NumberFormatterUtil.getRoundedNumberAsString(memory.read(state), NOF_DIGITS);
    }


}
