package chapters.ch8.plotting;

import chapters.ch8.domain.agent.memory.StateActionParking;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.trainer.core.TrainerDependenciesParking;
import core.foundation.config.PathAndFile;
import core.foundation.config.PlotConfig;
import core.foundation.util.collections.ListCreatorUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_saving_and_plotting.ChartSaver;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import java.awt.*;
import java.util.*;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentParkingMemoryCurvePlotter {

    public static final int NOF_DIGITS = 2;

    TrainerDependenciesParking dependencies;

    public static AgentParkingMemoryCurvePlotter of(TrainerDependenciesParking dependencies) {
        return new AgentParkingMemoryCurvePlotter(dependencies);
    }

    public void saveAndShow(String path, String parkActions, PlotConfig plotCfg) {
        XYChart chart = getChart(plotCfg);
        ChartSaver.saveXYChart(chart, PathAndFile.ofPng(path, parkActions));
        new SwingWrapper<>(chart).displayChart();
    }

    private XYChart getChart(PlotConfig plotCfg)  {
        int nSpots = dependencies.environment().getParameters().nSpots();
        var parameters = dependencies.environment().getParameters();
        var memory = dependencies.agent().getMemory();
        var xData = ListCreatorUtil.createFromZeroToNofItems(nSpots);
        var chartCreator = ManyLinesChartCreator.of(getPlotSettings(plotCfg),xData);
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

    private static PlotSettings getPlotSettings(PlotConfig plotCfg) {
        return PlotSettings.ofDefaults()
                .withWidth(plotCfg.xyChartWidth3Col()).withHeight(plotCfg.xyChartHeight())
                .withXAxisLabel("Nof. occupied").withYAxisLabel("Accept advantage")
                .withShowLegend(false)
                .withColorRangeSeries(new Color[]{Color.BLACK, Color.GRAY})
                .withShowMarker(false)
                .withMinCellMargin(0).withMaxCellMargin(0);
    }


}
