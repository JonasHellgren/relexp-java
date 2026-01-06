package chapters.ch4.plotting;

import core.foundation.config.PlotConfig;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.ArrayCreator;
import core.gridrl.EnvironmentGridI;
import core.plotting.base.shared.PlotSettings;
import core.plotting.plotting_3d.HeatMapWithStringTextInCellsCreator;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.HeatMapChart;

import java.awt.*;
import java.io.IOException;

/**
 * A utility class for creating heat maps with string text in cells.
 */
@UtilityClass
public class GridPlotHelper {

    public static HeatMapChart getStringTextChart(String[][] valueData,
                                                  int nCols,
                                                  int nRows,
                                                  PlotConfig plotCfg)  {
        var settings= PlotSettings.stringTextInHeatMap()
                .withWidth(plotCfg.xyChartWidth2Col()).withHeight(plotCfg.xyChartHeight())
                .withAnnotationTextFont(plotCfg.font())
                .withShowMarker(false)
                .withMinCellMargin(0).withMaxCellMargin(0);
        var creator = HeatMapWithStringTextInCellsCreator.ofStringData(
                settings,
                valueData,
                getXData(nCols),
                getYData(nRows));
        return creator.create();
    }

    public static int getNofRows(@NonNull EnvironmentGridI environment) {
        return environment.informer().getPosYMinMax().getSecond() + 1;
    }

    public static Integer getNofCols(@NonNull EnvironmentGridI environment) {
        return environment.informer().getPosXMinMax().getSecond()+1;
    }

    public static double[] getXData(int nCols) {
        return ArrayCreator.createArrayFromStartAndEnd(nCols,0, nCols-1);
    }

    public static double[] getYData(int nRows) {
        return ArrayCreator.createArrayFromStartAndEnd(nRows, 0, nRows-1);
    }


}
