package chapters.ch5.plotting;

import chapters.ch5.domain.memory.StateMemoryMcI;
import chapters.ch5.implem.dice.StateDice;
import chapters.ch5.implem.walk.EnvironmentWalk;
import chapters.ch5.implem.walk.StateWalk;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import org.knowm.xchart.HeatMapChart;
import java.awt.*;
import java.io.IOException;



/**
 * A utility class for plotting memory
 * Used by dice and random walk problems
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValueMemoryMcPlotter {

    @Builder
    public record Settings (
            int nofDigits, String envName, String fileNameAddOn,
            int nRows, int nCols, int startRow, int startColumn,
            String xLabel, String yLabel) {
    }

    private final Font VALUE_TEXT_FONT = new Font("Arial", Font.PLAIN, 12);

    StateMemoryMcI valueMemory;
    private final Settings settings;

    public static ValueMemoryMcPlotter of(StateMemoryMcI valueMemory,
                                          Settings settings) {
        return new ValueMemoryMcPlotter(valueMemory, settings);
    }

    @SneakyThrows
    public void plotStateValues() {
        ChartSaverAndPlotter.showAndSaveHeatMapFolderMonteCarlo(
                getValueChart(), "values_", settings.fileNameAddOn);
    }

    private HeatMapChart getValueChart() throws IOException {
        String[][] valueData = getStringData();
         var factory=HeatMapChartFactory.builder()
                 .xLabel(settings.xLabel).yLabel(settings.yLabel)
                 .valueData(valueData).nCols(settings.nCols).nRows(settings.nRows)
                 .startCol(settings.startColumn).font(VALUE_TEXT_FONT).build();

        return factory.getStringTextChart();
    }

    private String[][] getStringData() {
        String[][] valueData = new String[settings.nRows][settings.nCols];
        for (int y = settings.startRow; y < settings.nRows; y++) {
            for (int x = settings.startColumn; x < settings.startColumn+ settings.nCols; x++) {
                var state = settings.envName.equals(EnvironmentWalk.NAME)
                        ? StateWalk.of(x+(y-1)*100)
                        : StateDice.of(x, y);
                var xi=x-settings.startColumn;
                valueData[y][xi] = valueMemory.isPresent(state)
                        ? NumberFormatterUtil.getRoundedNumberAsString(valueMemory.read(state), settings.nofDigits)
                        : "." ;

                System.out.println("y = " + y+" x = "+x+" value = "+valueData[y][xi]);


            }
        }
        return valueData;
    }


}

