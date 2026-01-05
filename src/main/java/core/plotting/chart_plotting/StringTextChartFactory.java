package core.plotting.chart_plotting;

import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.ArrayCreator;
import core.plotting.base.shared.PlotSettings;
import core.plotting.plotting_3d.HeatMapWithStringTextInCellsCreator;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.HeatMapChart;

import java.awt.*;
import java.io.IOException;


@UtilityClass
public class StringTextChartFactory {


    public static HeatMapChart produce(String[][] valueData,
                                       int nCols,
                                       int nRows,
                                       Font font) throws IOException {
        var properties = ProjectPropertiesReader.create();
        var settings = PlotSettings.stringTextInHeatMap()
                .withWidth(properties.xyChartWidth2Col()).withHeight(properties.xyChartHeight())
                .withAnnotationTextFont(font)
                .withMinCellMargin(0).withMaxCellMargin(0);
        var creator = HeatMapWithStringTextInCellsCreator.ofStringData(
                settings,
                valueData,
                getXData(nCols),
                getYData(nRows));
        return creator.create();
    }

    public static double[] getXData(int nCols) {
        return ArrayCreator.createArrayFromStartAndEnd(nCols, 0, nCols - 1);
    }

    public static double[] getYData(int nRows) {
        return ArrayCreator.createArrayFromStartAndEnd(nRows, 0, nRows - 1);
    }

}



