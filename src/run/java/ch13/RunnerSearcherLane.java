package ch13;

import chapters.ch13.domain.searcher.node.Node;
import chapters.ch13.domain.searcher.path.OptimalPathExtractor;
import chapters.ch13.domain.searcher.searcher.Dependencies;
import chapters.ch13.domain.searcher.searcher.Searcher;
import chapters.ch13.environments.lane_change.ActionLane;
import chapters.ch13.environments.lane_change.EnvironmentLane;
import chapters.ch13.environments.lane_change.StateLane;
import chapters.ch13.factory.FactoryDependencies;
import chapters.ch13.factory.FactoryTreeForTest;
import chapters.ch13.plotting.DotFileGenerator;
import core.foundation.config.PathAndFile;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import core.foundation.util.collections.ListCreator;
import core.plotting.base.shared.PlotSettings;
import core.plotting.chart_plotting.ChartSaver;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;
import java.util.List;
import static core.foundation.util.unit_converter.MyUnitConverter.convertRadiansToDegrees;

/***
 *  dot -Tpng pictures/k_mcts/lane_runner_mcts.dot -o pictures/k_mcts/lane_runner_mcts.png
 */

public class RunnerSearcherLane {

    public static final int MAX_DEPTH_IN_PLOT = 2;
    public static final String FILE_NAME = "lane_runner_mcts.dot";

    static Dependencies<StateLane, ActionLane> dependencies;

    @SneakyThrows
    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        dependencies = FactoryDependencies.laneTest();
        var searcher = Searcher.of(dependencies);
        var root = FactoryTreeForTest.getRootLane();
        var tree = searcher.search(root);
        int sizeTree = tree.info().numberOfNodes();
        timer.printInMs();

        var pathExtractor = OptimalPathExtractor.of(dependencies);
        var generator = DotFileGenerator.init(MAX_DEPTH_IN_PLOT);
        var nodes = pathExtractor.extract(root).getNodes();
        var text = generator.generateDot(tree, nodes);
        ProjectPropertiesReader projectPropertiesReader = ProjectPropertiesReader.create();
        var filePath = projectPropertiesReader.pathMcts();
        generator.writeToFile(filePath + FILE_NAME, text);
        plotting(nodes, projectPropertiesReader, filePath);
    }

    private static void plotting(List<Node<StateLane, ActionLane>> nodes, ProjectPropertiesReader projectPropertiesReader, String filePath) {
        List<ActionLane> actions = nodes.stream().map(n -> n.info().action()).toList();
        var angleSteer = actions.stream()
                .map(n -> convertRadiansToDegrees(n.getSteeringAngle()))
                .toList();
        var angleHead = nodes.stream()
                .map(n -> convertRadiansToDegrees(n.info().state().headingAngle()))
                .toList();
        var yPos = nodes.stream().map(n -> n.info().state().y()).toList();
        double timeStepInSec=((EnvironmentLane) dependencies.environment()).getSettings().timeStep();
        var xList = ListCreator.createFromStartWithStepWithNofItems(0d, timeStepInSec*1000, angleHead.size());
        var chartAngles = getAnglesChart(projectPropertiesReader, xList, angleSteer,angleHead);
        var chartPos = getYPosChart(projectPropertiesReader, xList, yPos);
        ChartSaver.saveAndShowXYChart(chartAngles, PathAndFile.ofPng(filePath, "lane_angles.png"));
        ChartSaver.saveAndShowXYChart(chartPos, PathAndFile.ofPng(filePath, "lane_pos.png"));
    }

    private static XYChart getAnglesChart(ProjectPropertiesReader reader,
                                          List<Double> xList,
                                          List<Double> angleSteer,
                                          List<Double> angleHead) {
        var creatorAngles = ManyLinesChartCreator.of(getPlotSettings(reader, "Angle (Deg)"), xList);
        creatorAngles.addLine("steer", angleSteer);
        creatorAngles.addLine("heading", angleHead);
        return creatorAngles.create();
    }

    private static XYChart getYPosChart(ProjectPropertiesReader reader,
                                        List<Double> xList,
                                        List<Double> yPos) {
        var creatorAngles = ManyLinesChartCreator.of(getPlotSettings(reader, "Position (m)"), xList);
        creatorAngles.addLine("y", yPos);
        return creatorAngles.create();
    }

    private static PlotSettings getPlotSettings(ProjectPropertiesReader reader, String yAxisLabel) {
        return PlotSettings.ofDefaults()
                .withWidth(reader.xyChartWidth1Col())
                .withHeight(reader.xyChartHeight())
                .withLegendPosition(Styler.LegendPosition.InsideSE)
                .withXAxisLabel("Time (ms)")
                .withYAxisLabel(yAxisLabel);
    }
}
