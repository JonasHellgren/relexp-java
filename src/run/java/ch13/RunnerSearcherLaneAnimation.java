package ch13;

import chapters.ch13.animation.AnimationLaneChange;
import chapters.ch13.domain.searcher.core.OuterDependencies;
import chapters.ch13.domain.searcher.core.Searcher;
import chapters.ch13.factory.lane_change.FactoryDependenciesLaneChange;
import chapters.ch13.factory.lane_change.FactoryTreeLaneChange;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.StateLane;
import core.foundation.config.ConfigFactory;
import lombok.SneakyThrows;



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
