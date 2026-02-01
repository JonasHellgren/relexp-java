package chapters.ch12.plotting_invpend;

import chapters.ch12.domain.inv_pendulum.agent.memory.ActionAndItsValue;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.domain.inv_pendulum.trainer.core.TrainerDependencies;
import core.foundation.config.PathAndFile;
import core.foundation.util.collections.List2ArrayConverterUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.plotting_3d.HeatMapChartCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.SwingWrapper;
import java.awt.*;
import java.util.function.Function;

import static core.plotting_core.chart_plotting.ChartSaver.saveHeatMapChart;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PendulumAgentMemoryPlotter {

    public static final String TORQUE = "Torque (Nm)";
    public static final String VALUE = "Value";
    public static final int N_COL_ROWS_HEAT_MAP = 50;
    public static final String X_AXIS_LABEL = "Angular speed (deg/s)";
    public static final String Y_AXIS_LABEL = "Theta (deg)";
    public static final int FONT_SIZE_AXIS = 20;
    TrainerDependencies dependencies;

    public static PendulumAgentMemoryPlotter of(TrainerDependencies dependencies) {
        return new PendulumAgentMemoryPlotter(dependencies);
    }

    @SneakyThrows
    public void plotAndSaveAll(String pathPics) {
        plotAndSaveTorque(pathPics);
        plotAndSavValue(pathPics);
    }

    private void plotAndSaveTorque(String path) {
        Function<StatePendulum,Double> func = s ->
                dependencies.agent().chooseActionNoExploration(s).torque();
        var chart= createChart(TORQUE, getData(func), true);
        saveHeatMapChart(chart, PathAndFile.ofPng(path, TORQUE));
        new SwingWrapper<>(chart).displayChart();
    }

    private void plotAndSavValue(String path) {
        Function<StatePendulum,Double> func = s -> {
          var avList=dependencies.agent().read(s);
          return avList.stream()
                  .mapToDouble(ActionAndItsValue::actionValue)
                  .max()
                  .orElseThrow()*100;
        };
        var chart= createChart(VALUE, getData(func), false);
        saveHeatMapChart(chart, PathAndFile.ofPng(path, VALUE));
        new SwingWrapper<>(chart).displayChart();
    }


    private double[][] getData(Function<StatePendulum,Double> func) {
        var thetaList = dependencies.environment().getParameters().thetaSpace(N_COL_ROWS_HEAT_MAP);
        var spdList = dependencies.environment().getParameters().narrowSpdSpace(N_COL_ROWS_HEAT_MAP);
        double[][] data = new double[thetaList.size()][spdList.size()];
        for (double theta : thetaList) {
            for (double spd : spdList) {
                int yi = thetaList.indexOf(theta);
                int spdi = spdList.indexOf(spd);
                data[yi][spdi] = func.apply(StatePendulum.ofStart(theta, spd));
            }
        }
        return data;
    }


    @SneakyThrows
    private HeatMapChart createChart(String title, double[][] data, boolean showLegend) {
        Font ticksFont = new Font("Arial", Font.PLAIN, FONT_SIZE_AXIS);
        Font axisTitleFont = new Font("Arial", Font.BOLD, FONT_SIZE_AXIS);
        var settingsMap = PlotSettings.defaultBuilder()
                .xAxisLabel(X_AXIS_LABEL).yAxisLabel(Y_AXIS_LABEL)
                .axisTitleFont(axisTitleFont)
                .axisTicksFont(ticksFont)
                .showLegend(showLegend)
                .legendTextFont(ticksFont)
                .title(title).showDataValues(false).build();
        var pp = dependencies.environment().getParameters();
        var spdList = pp.narrowSpdSpaceInDegPerSec(N_COL_ROWS_HEAT_MAP);
        var thetaList = pp.thetaSpaceInDegrees(N_COL_ROWS_HEAT_MAP);
        var creator = HeatMapChartCreator.of(
                settingsMap,
                data,
                List2ArrayConverterUtil.convertListToDoubleArr(spdList),
                List2ArrayConverterUtil.convertListToDoubleArr(thetaList));
        return creator.create();
    }




}
