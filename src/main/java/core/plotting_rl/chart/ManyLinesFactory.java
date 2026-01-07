package core.plotting_rl.chart;

import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.ListCreator;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import core.plotting.base.shared.PlotSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.io.IOException;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ManyLinesFactory {

    private  String yLabel;
    private  String xLabel;
    private double spaceBetweenXTicks;
    private int nItems;

    public ManyLinesChartCreator getManyLinesChartCreator()
            throws IOException {
        var props = ProjectPropertiesReader.create();
        var inList = ListCreator.createFromStartWithStepWithNofItems(0,1d, nItems);
        var settings = PlotSettings.ofDefaults()
                .withXAxisLabel(xLabel).withYAxisLabel(yLabel)
                .withDefinedSpaceBetweenXTicks(true)
                .withSpaceBetweenXTicks(spaceBetweenXTicks)
                .withWidth(props.xyChartWidth1Col())
                .withHeight(props.xyChartHeight())
                .withAxisTicksDecimalFormat("#.#");
        return ManyLinesChartCreator.of(settings, inList);
    }

}
