package chapters.ch4.plotting;

import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.SwingWrapper;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

import static chapters.ch4.domain.helper.GridPlotHelper.*;


/**
 * This class is responsible for plotting a grid environment.
 * Showing where walls, terminal states etc are located
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GridEnvironmentPlotter {

    record CellType(boolean isWall, boolean isTerminal, boolean isFail) {}

   static final Map<CellType,String> cellTypeMap = Map.of(
            new CellType(true, false, false), "X",
            new CellType(false, true, false), "T",
            new CellType(false, true, true), "F"
    );

    public static final Font VALUE_TEXT_FONT = new Font("Arial", Font.PLAIN, 12);
    EnvironmentGridI environment;

    public static GridEnvironmentPlotter of(EnvironmentGridI environment) {
        return new GridEnvironmentPlotter(environment);
    }

    @SneakyThrows
    public void plot() {
        HeatMapChart chart = getValueChart();
        new SwingWrapper<>(chart).displayChart();
    }

    private HeatMapChart getValueChart() throws IOException {
        int nCols = getNofCols(environment);
        int nRows = getNofRows(environment);
        String[][] valueData = getValueData(nRows, nCols);
        return getStringTextChart(valueData, nCols, nRows, VALUE_TEXT_FONT);
    }

    private String[][] getValueData(int nRows, int nCols) {
        String[][] valueData = new String[nRows][nCols];
        var params= environment.getParameters();
        for (int y = 0; y < nRows; y++) {
            for (int x = 0; x < nCols; x++) {
                var state = StateGrid.of(x, y);
                var cellType= new CellType(
                        params.isWall(state), params.isTerminal(state), params.isFail(state));
                   valueData[y][x] = cellTypeMap.getOrDefault(cellType, ".");
            }
        }
        return valueData;
    }

}
