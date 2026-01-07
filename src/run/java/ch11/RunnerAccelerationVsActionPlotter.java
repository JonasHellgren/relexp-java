package ch11;

import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.ListCreator;
import core.foundation.util.unit_converter.MyUnitConverter;
import core.foundation.util.unit_converter.NonSIUnits;
import core.plotting.base.shared.PlotSettings;
import core.plotting_rl.chart.ChartCreatorFactory;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import core.plotting.plotting_2d.ScatterWithLineChartCreator;
import lombok.SneakyThrows;
import tec.units.ri.unit.Units;

import java.util.List;

public class RunnerAccelerationVsActionPlotter {

    public static final int START = -1000;
    public static final int END = 3000;
    public static final int STEP = 100;
    static String FILE_NAME = "acc_vs_action";

    public static void main(String[] args) {
        var ep = LunarParameters.defaultProps();
        var environment = (EnvironmentLunar) EnvironmentLunar.of(ep);
        var forces = ListCreator.createFromStartToEndWithStep(START, END, STEP);
        var accList = getAccList(forces, environment);
        var factory = getFactory();
        factory.addLine(forces, accList);
        var chart = factory.create();
        ChartSaverAndPlotter.showChartSaveInFolderActorCritic(chart, FILE_NAME);
    }

    @SneakyThrows
    private static ScatterWithLineChartCreator getFactory() {
        var props = ProjectPropertiesReader.create();
        var settings = PlotSettings.ofDefaults()
                .withXAxisLabel("Force (N)").withYAxisLabel("Acc. (m/s2)")
                .withWidth(props.xyChartWidth1Col())
                .withHeight(props.xyChartHeight())
                .withAxisTicksDecimalFormat("#.#");
        return ChartCreatorFactory.produceLineWithSettings(settings);
    }

    private static List<Double> getAccList(List<Double> forces, EnvironmentLunar environment) {
        return forces.stream()
                .map(force -> environment.acceleration(getForceInKn((int) Math.round(force))))
                .toList();
    }


    private static double getForceInKn(int intValue) {
        return MyUnitConverter.convertForce(intValue, Units.NEWTON, NonSIUnits.KILO_NEWTON);
    }

}
