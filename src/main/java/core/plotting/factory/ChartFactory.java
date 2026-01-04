package core.plotting.factory;

import core.plotting.chart_plotting.ChartCreatorFactory;
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
