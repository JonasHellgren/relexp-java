package chapters.ch2.factory;

import core.plotting.chart_plotting.PlotterFactory;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ManyLinesChartCreatorFactory {
    public static final double SPACE_BETWEEN_X_TICKS = 10d;
    public static final int NOF_TIME0_ITEMS = 1;


    @SneakyThrows
    public static ManyLinesChartCreator produce(int nofIterations) {
        var factor = PlotterFactory.builder()
                .xLabel("Iteration").yLabel("w")
                .spaceBetweenXTicks(SPACE_BETWEEN_X_TICKS)
                .nItems(nofIterations+ NOF_TIME0_ITEMS)
                .build();
        return  factor.getManyLinesChartCreator();
    }

}
