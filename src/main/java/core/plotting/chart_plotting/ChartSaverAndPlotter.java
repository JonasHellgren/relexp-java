package core.plotting.chart_plotting;

import core.foundation.configOld.PathAndFile;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import static core.plotting.chart_plotting.ProjectFoldersKeyInterpreter.*;

@UtilityClass
public class ChartSaverAndPlotter {

    public static void showAndSaveChart(XYChart chart, String fileName) {
        showAndSaveXyChart(chart, fileName, CONCEPT);
    }

    public static void showChartSaveInFolderTempDiff(XYChart chart, String fileName) {
        showAndSaveXyChart(chart, fileName, TD);
    }

    public static void showChartSaveInFolderMonteCarlo(XYChart chart, String fileName) {
        showAndSaveXyChart(chart, fileName, MC);
    }

    public static void showChartSaveInFolderActorCritic(XYChart chart, String fileName) {
        showAndSaveXyChart(chart, fileName, AC);
    }

    public static void showChartSaveInFolderGradientLearning(XYChart chart, String s) {
        showAndSaveXyChart(chart, s, GL);
    }

    public static void showChartSaveInFolderRbf(XYChart chart, String name) {
        showAndSaveXyChart(chart, name, RBF);
    }


    public static void showChartSaveInFolderAdvConceptsNeural(XYChart chart, String name) {
        showAndSaveXyChart(chart,name, ADVC_NEURAL);

    }


    public static void showChartSaveInFolderPolGrad(XYChart chart, String name) {
        showAndSaveXyChart(chart, name, POLGRAD);
    }

    public static void showChartSaveInFolderDeepRl(XYChart chart, String s) {
        showAndSaveXyChart(chart, s, DEEP_RL);
    }


    public static void showAndSaveHeatMapRbf(HeatMapChart chart, String fileName) {
        showAndSaveHeatMap(chart, "", fileName, RBF);
    }



    public static void showAndSaveHeatMapFolderTempDiff(HeatMapChart chart,
                                                        String policy,
                                                        String fileNameAddOn) {
        showAndSaveHeatMap(chart, policy, fileNameAddOn, TD);
    }

    public static void showAndSaveHeatMapFolderMonteCarlo(HeatMapChart chart,
                                                          String policy,
                                                          String fileNameAddOn) {
        showAndSaveHeatMap(chart, policy, fileNameAddOn, MC);
    }

    public static void showAndSaveHeatMapFolderMultiStep(HeatMapChart chart,
                                                         String policy,
                                                         String fileNameAddOn) {
        showAndSaveHeatMap(chart, policy, fileNameAddOn, MS );
    }

    public static void showAndSaveHeatMapFolderSafe(HeatMapChart chart,
                                                    String policy,
                                                    String fileNameAddOn) {
        showAndSaveHeatMap(chart, policy, fileNameAddOn, SAFE );
    }




    @SneakyThrows
    private static void showAndSaveXyChart(XYChart chart,
                                           String fileName,
                                           ProjectFoldersKeyInterpreter folderKey) {
        var path = getPath(folderKey);
        ChartSaver.saveXYChart(chart, PathAndFile.ofPng(path, fileName));
        new SwingWrapper<>(chart).displayChart();
    }

    @SneakyThrows
    private static void showAndSaveHeatMap(HeatMapChart chart,
                                           String policy,
                                           String fileName,
                                           ProjectFoldersKeyInterpreter folderKey) {
        var path = ProjectFoldersKeyInterpreter.getPath(folderKey);
        PathAndFile pathAndFile = PathAndFile.ofPng(path, policy + fileName);
        ChartSaver.saveHeatMapChart(chart, pathAndFile);
        new SwingWrapper<>(chart).displayChart();
    }


}
