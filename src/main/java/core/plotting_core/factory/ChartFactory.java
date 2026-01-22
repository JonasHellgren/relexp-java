package core.plotting_core.factory;

import core.plotting_rl.chart.ChartCreatorFactory;
import lombok.experimental.UtilityClass;
import org.knowm.xchart.XYChart;

import java.util.List;

@UtilityClass
public class ChartFactory {

    public static XYChart getChart(List<Double> xList, List<Double> yList) {
        var chartCreator = ChartCreatorFactory.produceXYLine();
        chartCreator.addLine(xList, yList);
        return chartCreator.create();
    }
}
