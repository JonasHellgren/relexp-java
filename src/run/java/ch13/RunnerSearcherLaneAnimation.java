package ch13;

import chapters.ch13.animation.AnimationLaneChange;
import chapters.ch13.domain.searcher.core.OuterDependencies;
import chapters.ch13.domain.searcher.core.Searcher;
import chapters.ch13.domain.searcher.path.OptimalPathExtractor;
import chapters.ch13.domain.tree.Node;
import chapters.ch13.factory.lane_change.FactoryDependenciesLaneChange;
import chapters.ch13.factory.lane_change.FactoryTreeLaneChange;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.EnvironmentLane;
import chapters.ch13.implem.lane_change.StateLane;
import chapters.ch13.plotting.DotFileGenerator;
import core.foundation.config.ConfigFactory;
import core.foundation.config.PathAndFile;
import core.foundation.config.PlotConfig;
import core.foundation.util.collections.ListCreatorUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_saving_and_plotting.ChartSaver;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.Styler;

import java.util.List;

import static core.foundation.util.unit_converter.UnitConverterUtil.convertRadiansToDegrees;

/***
 *  dot -Tpng pictures/ch13/lane_runner_mcts.dot -o pictures/ch13/lane_runner_mcts.png
 */

public class RunnerSearcherLaneAnimation {

    static String PATH = ConfigFactory.pathPicsConfig().ch13();
    public static final String FILE_NAME = "lane_runner_mcts.dot";

    static OuterDependencies<StateLane, ActionLane> dependencies;

    @SneakyThrows
    public static void main(String[] args) {

        dependencies = FactoryDependenciesLaneChange.animation();
        var searcher = Searcher.of(dependencies);
        var root = FactoryTreeLaneChange.onlyRoot();
        var cfg= ConfigFactory.getAnimationConfig();
        var tree = searcher.search(root, AnimationLaneChange.create(cfg));
        searcher.logTime();
    }

}
