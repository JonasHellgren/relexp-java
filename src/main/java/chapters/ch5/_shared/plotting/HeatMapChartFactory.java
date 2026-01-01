package chapters.ch5._shared.plotting;

import core.foundation.config.ProjectPropertiesReader;
import core.foundation.util.list_array.ArrayCreator;
import core.plotting.base.shared.PlotSettings;
import core.plotting.plotting_3d.HeatMapWithStringTextInCellsCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.knowm.xchart.HeatMapChart;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HeatMapChartFactory {

    @Builder.Default
    String xLabel="x";
    String yLabel="y";
    String[][] valueData;
    int nCols;
    int startCol;
    int nRows;
    Font font;

    public  HeatMapChart getStringTextChart() throws IOException {
        var properties = ProjectPropertiesReader.create();
        var settings= PlotSettings.stringTextInHeatMap()
                .withXAxisLabel(xLabel).withYAxisLabel(yLabel)
                .withWidth(properties.xyChartWidth2Col()).withHeight(properties.xyChartHeight())
                .withAnnotationTextFont(font)
                .withSpaceBetweenXTicks(1d)
                .withMinCellMargin(0).withMaxCellMargin(0);
        System.out.println("getXData(nCols, startCol) = " + Arrays.toString(getXData(nCols, startCol)));
        var creator = HeatMapWithStringTextInCellsCreator.ofStringData(
                settings,
                valueData,
                getXData(nCols, startCol),
                getYData(nRows));
        return creator.create();
    }


    public static double[] getXData(int nCols, int startCol) {
        return ArrayCreator.createArrayFromStartAndEnd(nCols, startCol,startCol+ nCols-1);
    }

    public static double[] getYData(int nRows) {
        return ArrayCreator.createArrayFromStartAndEnd(nRows, 0, nRows-1);
    }


}
