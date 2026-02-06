package chapters.ch3.plotting;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.implem.splitting_path_problem.InformerSplitting;
import core.foundation.config.PlotConfig;
import core.gridrl.StateValueMemoryGrid;
import core.foundation.config.PathAndFile;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.gridrl.StateGrid;
import core.plotting_core.chart_saving_and_plotting.ChartSaver;
import core.plotting_rl.chart.StringTextChartFactory;
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

    StateValueMemoryGrid memory;
    EnvironmentParametersSplitting parameters;

    @SneakyThrows
    public void plotAndSaveStateValues(String filePath, String fileName, PlotConfig plotCfg) {
        HeatMapChart chart = getValueChart(plotCfg);
        ChartSaver.saveHeatMapChart(chart, PathAndFile.ofPng(filePath, fileName));
        new SwingWrapper<>(chart).displayChart();
    }


    private HeatMapChart getValueChart(PlotConfig plotCfg) throws IOException {
        var informer= InformerSplitting.create(parameters);
        int nCols = informer.getPosXMinMax().getSecond()+1;
        int nRows = informer.getPosYMinMax().getSecond()+1;
        String[][] valueData = getStringData(nRows, nCols);
        return StringTextChartFactory.produce(valueData, nCols, nRows,plotCfg);
    }

    private String[][] getStringData(int nRows, int nCols) {
        String[][] valueData = new String[nRows][nCols];
        var informer= InformerSplitting.create(parameters);
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                var state = StateGrid.of(x, y);
                boolean isWall=parameters.wallStates().contains(state);
                boolean isTerminal=informer.isTerminal(state);
                valueData[y][x] = isWall || isTerminal ? "."  : getNumberAsString(state);
            }
        }
        return valueData;
    }

    private String getNumberAsString(StateGrid state) {
        return NumberFormatterUtil.getRoundedNumberAsString(memory.read(state), NOF_DIGITS);
    }


}
