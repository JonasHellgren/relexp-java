package core.plotting.chart_plotting;

import core.foundation.config.PathAndFile;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.*;

import static org.knowm.xchart.BitmapEncoder.saveBitmapWithDPI;

@UtilityClass
public class ChartSaver {

    public static final int DPI = 300;

    @SneakyThrows
    public static void saveHeatMapChart(HeatMapChart chart, PathAndFile pathAndFile) {
        saveBitmapWithDPI(chart, pathAndFile.fullName(), BitmapEncoder.BitmapFormat.PNG, DPI);
    }

    @SneakyThrows
    public static void saveXYChart(XYChart chart, PathAndFile pathAndFile) {
        saveBitmapWithDPI(chart, pathAndFile.fullName(), BitmapEncoder.BitmapFormat.PNG, DPI);
    }


    @SneakyThrows
    public static void saveAndShowXYChart(XYChart chart, PathAndFile pathAndFile) {
        saveBitmapWithDPI(chart, pathAndFile.fullName(), BitmapEncoder.BitmapFormat.PNG, DPI);
        new SwingWrapper(chart).displayChart();
    }


    @SneakyThrows
    public static void saveAndShowXYChart(CategoryChart chart, PathAndFile pathAndFile) {
        saveBitmapWithDPI(chart, pathAndFile.fullName(), BitmapEncoder.BitmapFormat.PNG, DPI);
        new SwingWrapper(chart).displayChart();
    }


}
