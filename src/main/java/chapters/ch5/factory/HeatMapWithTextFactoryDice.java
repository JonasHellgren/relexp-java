package chapters.ch5.factory;

import chapters.ch5.implem.dice.StateDice;
import chapters.ch5.implem.dice.StateMemoryDice;
import core.foundation.config.PlotConfig;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.plotting.base.shared.PlotSettings;
import core.plotting.plotting_3d.HeatMapWithStringTextInCellsCreator;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.HeatMapChart;

@UtilityClass
public class HeatMapWithTextFactoryDice {

    public static final int NOF_DIGITS = 1;

    private final GridProperties GRID_PROPS =GridProperties.builder()
            .startCol(0).nCols(7).startRow(0).nRows(2)
            .build();

    public static HeatMapChart produce(StateMemoryDice stateMemory, PlotConfig cfg)  {
        var creator = HeatMapWithStringTextInCellsCreator.ofStringData(
                getPlotSettings(cfg),
                getStringData(stateMemory),
                GRID_PROPS.getXData(),
                GRID_PROPS.getYData());
        return creator.create();
    }

    private static PlotSettings getPlotSettings(PlotConfig cfg) {
        return PlotSettings.stringTextInHeatMap()
                .withXAxisLabel("x").withYAxisLabel("y")
                .withWidth(cfg.xyChartWidth2Col())
                .withHeight(cfg.xyChartHeight())
                .withAnnotationTextFont(cfg.font())
                .withSpaceBetweenXTicks(1d)
                .withMinCellMargin(0).withMaxCellMargin(0);
    }

    private static String[][] getStringData(StateMemoryDice mem) {
        String[][] valueData = GRID_PROPS.getEmptyValueData();
        for (int y = 0; y < GRID_PROPS.nRows(); y++) {
            for (int x = GRID_PROPS.startCol(); x < GRID_PROPS.endCol(); x++) {
                var state =StateDice.of(x, y);
                var xi=x-GRID_PROPS.startCol();
                valueData[y][xi] = mem.isPresent(state)
                        ? NumberFormatterUtil.getRoundedNumberAsString(mem.read(state), NOF_DIGITS)
                        : "." ;
            }
        }
        return valueData;
    }


}
