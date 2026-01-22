package core.plotting_rl.chart;

import core.foundation.config.ConfigFactory;
import core.plotting_core.plotting_2d.ScatterWithLineChartCreator;
import core.plotting_core.base.shared.PlotSettings;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;


/**
 * Chart creator factory
 */

@UtilityClass
public class ChartCreatorFactory {


    @SneakyThrows
    public static ScatterWithLineChartCreator produceXYLine() {
        var props= ConfigFactory.plotConfig();
        var settings = PlotSettings.ofDefaults()
                .withXAxisLabel("x").withYAxisLabel("y")
                .withWidth(props.xyChartWidth1Col())
                .withHeight(props.xyChartHeight())
                .withAxisTicksDecimalFormat("#");
        return ScatterWithLineChartCreator.of(settings);
    }


    @SneakyThrows
    public static ScatterWithLineChartCreator produceLine(String xLabel, String yLabel) {
        var props= ConfigFactory.plotConfig();
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
