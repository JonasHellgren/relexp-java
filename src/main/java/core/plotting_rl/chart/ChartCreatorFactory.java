package core.plotting_rl.chart;

import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting.plotting_2d.ScatterWithLineChartCreator;
import core.plotting.base.shared.PlotSettings;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;


/**
 * Chart creator factory
 */

@UtilityClass
public class ChartCreatorFactory {


    @SneakyThrows
    public static ScatterWithLineChartCreator produceXYLine() {
        var props = ProjectPropertiesReader.create();
        var settings = PlotSettings.ofDefaults()
                .withXAxisLabel("x").withYAxisLabel("y")
                .withWidth(props.xyChartWidth1Col())
                .withHeight(props.xyChartHeight())
                .withAxisTicksDecimalFormat("#");
        return ScatterWithLineChartCreator.of(settings);
    }


    @SneakyThrows
    public static ScatterWithLineChartCreator produceLine(String xLabel, String yLabel) {
        var props = ProjectPropertiesReader.create();
        var settings = PlotSettings.ofDefaults()
                .withXAxisLabel(xLabel).withYAxisLabel(yLabel)
                .withWidth(props.xyChartWidth1Col())
                .withHeight(props.xyChartHeight())
                .withAxisTicksDecimalFormat("#.#");
        return ScatterWithLineChartCreator.of(settings);
    }


    @SneakyThrows
    public static ScatterWithLineChartCreator produceLineWithSettings(PlotSettings settings) {
        return ScatterWithLineChartCreator.of(settings);
    }



}
