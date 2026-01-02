package ch9;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RunnerHelper {

    /**
     * Changing order of save and show gives different rendering results
     */


/*    @SneakyThrows
    public static void showAndSave(XYChart chart, String pathKey, String fileName) {
        var pathPics = ProjectPropertiesReader.create().getStringProperty(pathKey);
        ChartSaver.saveXYChart(chart, PathAndFile.ofPng(pathPics, fileName));
        new SwingWrapper<>(chart).displayChart();
    }*/

/*    @SneakyThrows
    public static void showAndSaveHeatMap(HeatMapChart chart, String rbfPics, String title) {
        var pathPics = ProjectPropertiesReader.create().getStringProperty(rbfPics);
        ChartSaver.saveHeatMapChart(chart, PathAndFile.ofPng(pathPics, title));
        new SwingWrapper<>(chart).displayChart();
    }*/
}
