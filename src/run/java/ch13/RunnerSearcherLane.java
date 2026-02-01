package ch13;

import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.searcher.path.OptimalPathExtractor;
import chapters.ch13.domain.searcher.core.OuterDependencies;
import chapters.ch13.domain.searcher.core.Searcher;
import chapters.ch13.factory.lane_change.FactoryTreeLaneChange;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.EnvironmentLane;
import chapters.ch13.implem.lane_change.StateLane;
import chapters.ch13.factory.lane_change.FactoryDependenciesLaneChange;
import chapters.ch13.plotting.DotFileGenerator;
import core.foundation.config.ConfigFactory;
import core.foundation.config.PathAndFile;
import core.foundation.config.PlotConfig;
import core.foundation.util.collections.ListCreatorUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_plotting.ChartSaver;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import java.util.List;
import static core.foundation.util.unit_converter.UnitConverterUtil.convertRadiansToDegrees;

/***
 *  dot -Tpng pictures/ch13/lane_runner_mcts.dot -o pictures/ch13/lane_runner_mcts.png
 */

public class RunnerSearcherLane {

    public static final int MAX_DEPTH_IN_PLOT = 2;
    static String PATH = ConfigFactory.pathPicsConfig().ch13();
    public static final String FILE_NAME = "lane_runner_mcts.dot";

    static OuterDependencies<StateLane, ActionLane> dependencies;

    @SneakyThrows
    public static void main(String[] args) {

        dependencies = FactoryDependenciesLaneChange.runner();
        var searcher = Searcher.of(dependencies);
        var root = FactoryTreeLaneChange.onlyRoot();
        var tree = searcher.search(root);
        searcher.logTime();

        var pathExtractor = OptimalPathExtractor.of(dependencies);
        var generator = DotFileGenerator.init(MAX_DEPTH_IN_PLOT);
        var nodes = pathExtractor.extract(root).getNodes();
        var text = generator.generateDot(tree, nodes);

        var plotConfig= ConfigFactory.plotConfig();
        generator.writeToFile(PATH + FILE_NAME, text);
        plotting(nodes, plotConfig);
    }

    private static void plotting(List<Node<StateLane, ActionLane>> nodes,
                                 PlotConfig plotConfig) {
        List<ActionLane> actions = nodes.stream().map(n -> n.info().action()).toList();
        var angleSteer = actions.stream()
                .map(n -> convertRadiansToDegrees(n.getSteeringAngle()))
                .toList();
        var angleHead = nodes.stream()
                .map(n -> convertRadiansToDegrees(n.info().state().headingAngle()))
                .toList();
        var yPos = nodes.stream().map(n -> n.info().state().y()).toList();
        double timeStepInSec=((EnvironmentLane) dependencies.environment()).getParameters().timeStep();
        var xList = ListCreatorUtil.createFromStartWithStepWithNofItems(0d, timeStepInSec*1000, angleHead.size());
        var chartAngles = getAnglesChart(xList, angleSteer,angleHead, getPlotSettings(plotConfig, "Angle (Deg)"));
        var chartPos = getYPosChart(xList, yPos, getPlotSettings(plotConfig, "Position (m)"));
        ChartSaver.saveAndShowXYChart(chartAngles, PathAndFile.ofPng(PATH, "lane_angles.png"));
        ChartSaver.saveAndShowXYChart(chartPos, PathAndFile.ofPng(PATH, "lane_pos.png"));
    }

    private static XYChart getAnglesChart(List<Double> xList,
                                          List<Double> angleSteer,
                                          List<Double> angleHead, PlotSettings plotSettings) {
        var creatorAngles = ManyLinesChartCreator.of(plotSettings, xList);
        creatorAngles.addLine("steer", angleSteer);
        creatorAngles.addLine("heading", angleHead);
        return creatorAngles.create();
    }

    private static XYChart getYPosChart(List<Double> xList,
                                        List<Double> yPos, PlotSettings plotSettings) {
        var creatorAngles = ManyLinesChartCreator.of(plotSettings, xList);
        creatorAngles.addLine("y", yPos);
        return creatorAngles.create();
    }

    private static PlotSettings getPlotSettings(PlotConfig plotConfig, String yAxisLabel) {
        return PlotSettings.ofDefaults()
                .withWidth(plotConfig.xyChartWidth1Col())
                .withHeight(plotConfig.xyChartHeight())
                .withLegendPosition(Styler.LegendPosition.InsideSE)
                .withXAxisLabel("Time (ms)")
                .withYAxisLabel(yAxisLabel);
    }
}
