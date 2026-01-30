package chapters.ch11.plotting;

import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import core.foundation.config.PathAndFile;
import core.foundation.util.collections.List2ArrayConverterUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.plotting_3d.HeatMapChartCreator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.SwingWrapper;
import java.awt.*;
import java.util.function.Function;

import static core.plotting_core.chart_plotting.ChartSaver.saveHeatMapChart;


/**
 * A utility class for plotting heat maps of agent data.
 */
@AllArgsConstructor
public class PlotterHeatMapsAgent {

    public static final String FORCE = "Force (N)";
    public static final String ACC_TITLE = "Acceleration (m/s2)";
    public static final String ACC_FILE = "Acceleration";
    public static final String VALUE = "Value";
    public static final int N_COL_ROWS_HEAT_MAP = 50;
    public static final String X_AXIS_LABEL = "Speed (m/s)";
    public static final String Y_AXIS_LABEL = "Position (m)";
    public static final int FONT_SIZE_AXIS = 20;

    private final TrainerDependencies dependencies;

    public static PlotterHeatMapsAgent of(TrainerDependencies trainerDependencies) {
        return new PlotterHeatMapsAgent(trainerDependencies);
    }

    @SneakyThrows
    public void plotAndSaveAll(String pathPics) {
        plotAndSaveExpectedForce(pathPics);
        plotAndSavExpectedAcceleration(pathPics);
        plotAndSavValue(pathPics);
    }

    private void plotAndSaveExpectedForce(String path) {
        Function<StateLunar,Double> func = s -> dependencies.agent().readActor(s).mean();
        var chart= createChart(FORCE, getData(func));
        saveHeatMapChart(chart, PathAndFile.ofPng(path, FORCE));
        new SwingWrapper<>(chart).displayChart();
    }

    private void plotAndSavExpectedAcceleration(String path) {
        var env=(EnvironmentLunar) dependencies.environment();
        Function<StateLunar,Double> func = s ->
                env.acceleration(dependencies.agent().readActor(s).mean());
        var chart= createChart(ACC_TITLE, getData(func));
        saveHeatMapChart(chart, PathAndFile.ofPng(path, ACC_FILE));
        new SwingWrapper<>(chart).displayChart();
    }

    private void plotAndSavValue(String path) {
        Function<StateLunar,Double> func = s -> dependencies.agent().readCritic(s);
        var chart= createChart(VALUE, getData(func));
        saveHeatMapChart(chart, PathAndFile.ofPng(path, VALUE));
        new SwingWrapper<>(chart).displayChart();
    }

    private double[][] getData(Function<StateLunar,Double> func) {
        var yList = dependencies.environment().getParameters().ySpace(N_COL_ROWS_HEAT_MAP);
        var spdList = dependencies.environment().getParameters().spdSpace(N_COL_ROWS_HEAT_MAP);
        double[][] data = new double[yList.size()][spdList.size()];
        for (double y : yList) {
            for (double spd : spdList) {
                int yi = yList.indexOf(y);
                int spdi = spdList.indexOf(spd);
                data[yi][spdi] = func.apply(StateLunar.of(y, spd));
            }
        }
        return data;
    }

    private HeatMapChart createChart(String title, double[][] data) {
        Font ticksFont = new Font("Arial", Font.PLAIN, FONT_SIZE_AXIS);
        Font axisTitleFont = new Font("Arial", Font.BOLD, FONT_SIZE_AXIS);
        var settingsMap = PlotSettings.defaultBuilder()
                .xAxisLabel(X_AXIS_LABEL).yAxisLabel(Y_AXIS_LABEL)
                .axisTitleFont(axisTitleFont)
                .axisTicksFont(ticksFont)
                .legendTextFont(ticksFont)
                .title(title).showDataValues(false).build();
        var yList = dependencies.environment().getParameters().ySpace(N_COL_ROWS_HEAT_MAP);
        var spdList = dependencies.environment().getParameters().spdSpace(N_COL_ROWS_HEAT_MAP);
        var creator = HeatMapChartCreator.of(
                settingsMap,
                data,
                List2ArrayConverterUtil.convertListToDoubleArr(spdList),
                List2ArrayConverterUtil.convertListToDoubleArr(yList));
        return creator.create();
    }



}
