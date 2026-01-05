package chapters.ch12.inv_pendulum.factory;

import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting.base.shared.PlotSettings;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.style.Styler;
import java.awt.*;
import java.util.List;

@UtilityClass
public class ManyLinesChartCreatorFactory {

    public static final int FONT_SIZE_AXIS = 15;
    public static final int FONT_SIZE_LEGEND = 12;

    static Font TICKS_FONT = new Font("Arial", Font.PLAIN, FONT_SIZE_AXIS);
    static Font AXIST_TITLE_FONT = new Font("Arial", Font.BOLD, FONT_SIZE_AXIS);
    static Font LEGEND_FONT = new Font("Arial", Font.BOLD, FONT_SIZE_LEGEND);


    @SneakyThrows
    public static ManyLinesChartCreator createChartCreatorForSimulation(
            String title, List<Double> timeTraj, boolean showLegend) {
        var width = ProjectPropertiesReader.create().xyChartWidth1Col();
        var height = ProjectPropertiesReader.create().xyChartHeight() * 1.5;

        PlotSettings settings = PlotSettings.ofDefaults()
                .withTitle(title).withXAxisLabel("Time (s)").withYAxisLabel("")
                .withDefinedSpaceBetweenXTicks(false)
                .withWidth(width).withHeight((int) height)
                .withLegendTextFont(TICKS_FONT)
                .withAxisTitleFont(AXIST_TITLE_FONT)
                .withAxisTicksFont(TICKS_FONT)
                .withShowLegend(showLegend)
                .withLegendTextFont(LEGEND_FONT)
                .withAxisTicksDecimalFormat("#.##")
                .withLegendPosition(Styler.LegendPosition.OutsideE)
                .withColorRangeSeries(
                        new Color[]{Color.BLACK, Color.GRAY, Color.LIGHT_GRAY});
        return ManyLinesChartCreator.of(
                settings,
                timeTraj);
    }


    @SneakyThrows
    public static ManyLinesChartCreator createChartCreatorForEvaluator(
            String title, List<Double> timeTraj, String xAxisLabel) {
        var width = ProjectPropertiesReader.create().xyChartWidth2Col();
        var height = ProjectPropertiesReader.create().xyChartHeight() * 1.5;

        PlotSettings settings = PlotSettings.ofDefaults()
                .withTitle(title).withXAxisLabel(xAxisLabel).withYAxisLabel("")
                .withDefinedSpaceBetweenXTicks(true)
                .withSpaceBetweenXTicks(2500d)
                .withWidth(width).withHeight((int) height)
                .withLegendTextFont(TICKS_FONT)
                .withAxisTitleFont(AXIST_TITLE_FONT)
                .withAxisTicksFont(TICKS_FONT)
                .withLegendTextFont(LEGEND_FONT)
                .withAxisTicksDecimalFormat("#.##")
                .withShowLegend(false)
                .withColorRangeSeries(
                        new Color[]{Color.BLACK, Color.GRAY, Color.LIGHT_GRAY});
        return ManyLinesChartCreator.of(
                settings,
                timeTraj);
    }

    @SneakyThrows
    public static ManyLinesChartCreator createChartCreatorForActionValues(
            String title, List<Double> timeTraj, boolean showLegend) {
        var width = ProjectPropertiesReader.create().xyChartWidth1Col();
        var height = ProjectPropertiesReader.create().xyChartHeight() * 1.5;

        PlotSettings settings = PlotSettings.ofDefaults()
                .withTitle(title).withXAxisLabel("Episode").withYAxisLabel("Value")
                .withDefinedSpaceBetweenXTicks(true).withSpaceBetweenXTicks(10d)
                .withWidth(width).withHeight((int) height)
                .withLegendTextFont(TICKS_FONT)
                .withAxisTitleFont(AXIST_TITLE_FONT)
                .withAxisTicksFont(TICKS_FONT)
                .withShowLegend(showLegend)
                .withLegendTextFont(LEGEND_FONT)
                .withAxisTicksDecimalFormat("#.##")
                .withLegendPosition(Styler.LegendPosition.OutsideE)
                .withColorRangeSeries(
                        new Color[]{Color.BLACK, Color.GRAY, Color.LIGHT_GRAY});
        return ManyLinesChartCreator.of(
                settings,
                timeTraj);
    }

}
