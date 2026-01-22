package core.plotting_rl.chart;

import core.foundation.config.PlotConfig;
import core.foundation.util.collections.ArrayCreatorUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.plotting_3d.HeatMapWithStringTextInCellsCreator;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.HeatMapChart;

import java.io.IOException;


@UtilityClass
public class StringTextChartFactory {


    public static HeatMapChart produce(String[][] valueData,
                                       int nCols,
                                       int nRows,
                                       PlotConfig plotCfg) throws IOException {

        var settings = PlotSettings.stringTextInHeatMap()
                .withWidth(plotCfg.xyChartWidth2Col()).withHeight(plotCfg.xyChartHeight())
                .withAnnotationTextFont(plotCfg.font())
                .withMinCellMargin(0).withMaxCellMargin(0);
        var creator = HeatMapWithStringTextInCellsCreator.ofStringData(
                settings,
                valueData,
                getXData(nCols),
                getYData(nRows));
        return creator.create();
    }

    public static double[] getXData(int nCols) {
        return ArrayCreatorUtil.createArrayFromStartAndEnd(nCols, 0, nCols - 1);
    }

    public static double[] getYData(int nRows) {
        return ArrayCreatorUtil.createArrayFromStartAndEnd(nRows, 0, nRows - 1);
    }

}



