package core.plotting_rl.chart;

import core.foundation.config.PlotConfig;
import core.foundation.util.collections.ListCreatorUtil;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import core.plotting_core.base.shared.PlotSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ManyLinesFactory {

    private  String yLabel;
    private  String xLabel;
    private double spaceBetweenXTicks;
    private int nItems;

    public ManyLinesChartCreator getManyLinesChartCreator(PlotConfig plotCfg) {
        var inList = ListCreatorUtil.createFromStartWithStepWithNofItems(0,1d, nItems);
        var settings = PlotSettings.ofDefaults()
                .withXAxisLabel(xLabel).withYAxisLabel(yLabel)
                .withDefinedSpaceBetweenXTicks(true)
                .withSpaceBetweenXTicks(spaceBetweenXTicks)
                .withWidth(plotCfg.xyChartWidth1Col())
                .withHeight(plotCfg.xyChartHeight())
                .withAxisTicksDecimalFormat("#.#");
        return ManyLinesChartCreator.of(settings, inList);
    }

}
