package chapters.ch5.factory;

import chapters.ch5.domain.memory.StateMemoryMcI;
import chapters.ch5.implem.walk.StateWalk;
import core.foundation.config.PlotConfig;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.plotting_3d.HeatMapWithStringTextInCellsCreator;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.HeatMapChart;

@UtilityClass
public class HeatMapWithTextFactoryWalk {

    public static final int NOF_DIGITS = 1;

    private final GridProperties GRID_PROPS =GridProperties.builder()
            .startCol(1).nCols(6).startRow(0).nRows(3)
            .build();

    public static HeatMapChart produce(StateMemoryMcI stateMemory, PlotConfig cfg)  {
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

    private static String[][] getStringData(StateMemoryMcI mem) {
        String[][] valueData = GRID_PROPS.getEmptyValueData();
        for (int y = GRID_PROPS.startRow(); y < GRID_PROPS.nRows(); y++) {
            for (int x = GRID_PROPS.startCol(); x < GRID_PROPS.endCol(); x++) {
                var state = StateWalk.of(x+(y-1)*100);
                var xi=x-GRID_PROPS.startCol();
                valueData[y][xi] = mem.isPresent(state)
                        ? NumberFormatterUtil.getRoundedNumberAsString(mem.read(state), NOF_DIGITS)
                        : "." ;
            }
        }
        return valueData;
    }


}
