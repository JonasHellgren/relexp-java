package chapters.ch8.plotting;

import chapters.ch8.domain.agent.memory.StateActionParking;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.trainer.core.TrainerDependenciesParking;
import core.foundation.configOld.PathAndFile;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.ListCreator;
import core.plotting.base.shared.PlotSettings;
import core.plotting.chart_plotting.ChartSaver;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentParkingMemoryCurvePlotter {

    public static final Font VALUE_TEXT_FONT = new Font("Arial", Font.PLAIN, 12);
    public static final int NOF_DIGITS = 2;

    TrainerDependenciesParking dependencies;

    public static AgentParkingMemoryCurvePlotter of(TrainerDependenciesParking dependencies) {
        return new AgentParkingMemoryCurvePlotter(dependencies);
    }

    @SneakyThrows
    public void plotMemory(double feeCharging) {
        var path = ProjectPropertiesReader.create().pathNonEpisodic();
        saveAndShow(path, "park_values_curve_fee"+feeCharging);
    }

    private void saveAndShow(String path, String parkActions) throws IOException {
        XYChart chart = getChart();
        ChartSaver.saveXYChart(chart, PathAndFile.ofPng(path, parkActions));
        new SwingWrapper<>(chart).displayChart();
    }

    private XYChart getChart() throws IOException {
        int nSpots = dependencies.environment().getParameters().nSpots();
        var parameters = dependencies.environment().getParameters();
        var memory = dependencies.agent().getMemory();
        var properties = ProjectPropertiesReader.create();
        var xData = ListCreator.createFromZeroToNofItems(nSpots);
        var chartCreator = ManyLinesChartCreator.of(getPlotSettings(properties),xData);
        Map<FeeEnum, List<Double>> diffeMap = new HashMap<>();
        for (FeeEnum fee : FeeEnum.allFees()) {
            List<Double> diffList = new ArrayList<>();
            for (double nOccup : xData) {
                var s = StateParking.ofStart(parameters.nOccupAsInt(nOccup), fee);
                var saAcc = StateActionParking.of(s, ActionParking.ACCEPT);
                var saRej = StateActionParking.of(s, ActionParking.REJECT);
                diffList.add(memory.read(saAcc) - memory.read(saRej));
            }
            diffeMap.put(fee, diffList);
        }

        FeeEnum.allFees().forEach(k -> chartCreator.addLine(k.name(), diffeMap.get(k)));
        return chartCreator.create();

    }

    private static PlotSettings getPlotSettings(ProjectPropertiesReader properties) {
        //.withAnnotationTextFont(font)
        return PlotSettings.ofDefaults()
                .withWidth(properties.xyChartWidth3Col()).withHeight(properties.xyChartHeight())
                .withXAxisLabel("Nof. occupied").withYAxisLabel("Accept advantage")
                .withShowLegend(false)
                .withColorRangeSeries(new Color[]{Color.BLACK, Color.GRAY})
                .withShowMarker(false)
                .withMinCellMargin(0).withMaxCellMargin(0);
    }


}
