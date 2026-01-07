package chapters.ch8.plotting;

import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.trainer.core.TrainerDependenciesParking;
import core.foundation.configOld.PathAndFile;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.plotting.base.shared.PlotSettings;
import core.plotting.chart_plotting.ChartSaver;
import core.plotting.plotting_3d.HeatMapWithStringTextInCellsCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;
import org.knowm.xchart.HeatMapChart;
import org.knowm.xchart.SwingWrapper;
import java.awt.*;
import java.io.IOException;

import static core.plotting_rl.chart.StringTextChartFactory.getXData;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentParkingMemoryHeatMapPlotter {

    public static final Font VALUE_TEXT_FONT = new Font("Arial", Font.PLAIN, 12);
    public static final int NOF_DIGITS = 1;

    TrainerDependenciesParking dependencies;

    public static AgentParkingMemoryHeatMapPlotter of(TrainerDependenciesParking dependencies) {
        return new AgentParkingMemoryHeatMapPlotter(dependencies);
    }

    @SneakyThrows
    public void plotMemory(Pair<Double, Double> fees) {
        var path = ProjectPropertiesReader.create().pathNonEpisodic();
        saveAndShow(path, "park_actions_fee" + fees.getSecond(), fees, true);
        saveAndShow(path, "park_values_fee" + fees.getSecond(), fees, false);
    }

    private void saveAndShow(String path, String parkActions, Pair<Double, Double> fees, boolean isActions) throws IOException {
        HeatMapChart chart = getValueChart(isActions, fees);
        ChartSaver.saveHeatMapChart(chart, PathAndFile.ofPng(path, parkActions));
        new SwingWrapper<>(chart).displayChart();
    }

    private HeatMapChart getValueChart(boolean isActions, Pair<Double, Double> fees) throws IOException {
        var parameters = dependencies.environment().getParameters();
        int nCols = parameters.nSpots();
        int nRows = ActionParking.maxNofActions();
        String[][] valueData = getValueData(nRows, nCols, isActions);
        return getStringTextChart(valueData, nCols, fees);
    }

    private String[][] getValueData(int nRows, int nCols, boolean isActions) {
        String[][] valueData = new String[nRows][nCols];
        var agent = dependencies.agent();
        for (int feeIdx = 0; feeIdx < nRows; feeIdx++) {
            for (int nOccup = 0; nOccup < nCols; nOccup++) {
                var s = StateParking.ofStart(nOccup, FeeEnum.allFees().get(feeIdx));
                valueData[feeIdx][nOccup] = isActions
                        ? agent.chooseActionNoExploration(s).getLetter()
                        : NumberFormatterUtil.getRoundedNumberAsString(agent.value(s), NOF_DIGITS);
            }
        }
        return valueData;
    }

    public static HeatMapChart getStringTextChart(String[][] valueData,
                                                  int nCols,
                                                  Pair<Double, Double> fees) throws IOException {
        var properties = ProjectPropertiesReader.create();
        var settings = PlotSettings.stringTextInHeatMap()
                .withWidth(properties.xyChartWidth3Col()).withHeight(properties.xyChartHeight())
                .withXAxisLabel("Nof. occupied").withYAxisLabel("Fee")
                .withAnnotationTextFont(VALUE_TEXT_FONT)
                .withShowMarker(false)
                .withMinCellMargin(0).withMaxCellMargin(0);
        var creator = HeatMapWithStringTextInCellsCreator.ofStringData(
                settings,
                valueData,
                getXData(nCols),
                new double[]{fees.getFirst(), fees.getSecond()});
        return creator.create();
    }


}
