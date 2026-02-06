package core.plotting_core.chart_saving_and_plotting;

import core.foundation.config.PathAndFile;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.*;

import static org.knowm.xchart.BitmapEncoder.saveBitmapWithDPI;

@UtilityClass
public class ChartSaver {

    public static final int DPI = 300;

    @SneakyThrows
    public static void saveAndShowXYChart(CategoryChart chart, PathAndFile pathAndFile) {
        saveBitmapWithDPI(chart, pathAndFile.fullName(), BitmapEncoder.BitmapFormat.PNG, DPI);
        new SwingWrapper(chart).displayChart();
    }


    public static void saveAndShowXYChart(XYChart chart, String path, String fileName) {
        saveXYChart(chart,path, fileName);
        new SwingWrapper<>(chart).displayChart();
    }


    @SneakyThrows
    public static void saveAndShowXYChart(XYChart chart, PathAndFile pathAndFile) {
        saveBitmapWithDPI(chart, pathAndFile.fullName(), BitmapEncoder.BitmapFormat.PNG, DPI);
        new SwingWrapper(chart).displayChart();
    }

    @SneakyThrows
    public static void saveHeatMapChart(HeatMapChart chart, PathAndFile pathAndFile) {
        saveBitmapWithDPI(chart, pathAndFile.fullName(), BitmapEncoder.BitmapFormat.PNG, DPI);
    }

    @SneakyThrows
    public static void saveXYChart(XYChart chart, String path, String file) {
        saveXYChart(chart,PathAndFile.of(path, file));
    }


    @SneakyThrows
    public static void saveXYChart(XYChart chart, PathAndFile pathAndFile) {
        saveBitmapWithDPI(chart, pathAndFile.pathAndName(), BitmapEncoder.BitmapFormat.PNG, DPI);
    }


}
