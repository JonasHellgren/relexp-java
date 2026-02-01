package chapters.ch13;


import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.searcher.path.OptimalPathExtractor;
import chapters.ch13.domain.searcher.core.Dependencies;
import chapters.ch13.domain.searcher.core.Searcher;
import chapters.ch13.factory.lane_change.FactoryTreeLaneChange;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.StateLane;
import chapters.ch13.factory.lane_change.FactoryDependenciesLaneChange;
import chapters.ch13.factory.jumper.FactoryTreeJumper;
import chapters.ch13.plotting.DotFileGenerator;
import core.foundation.configOld.ProjectPropertiesReader;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/***
 *  dot -Tpng pictures/k_mcts/lane_test_mcts.dot -o pictures/k_mcts/lane_test_mcts.png
 */


public class TestSearcherLane {

    public static final int MAX_DEPTH_IN_PLOT = 2;
    Dependencies<StateLane, ActionLane> dependencies;
    Searcher<StateLane, ActionLane> searcher;
    Node<StateLane, ActionLane> root;
    public static final String FILE_NAME = "lane_test_mcts.dot";

    @BeforeEach
    void init() {
        dependencies = FactoryDependenciesLaneChange.laneTest();
        searcher = Searcher.of(dependencies);
        root = FactoryTreeLaneChange.onlyRoot();
    }

    @SneakyThrows
    @Test
    void whenSearchThenCorrect() {
        var tree = searcher.search(root);
        int sizeTree = tree.info().numberOfNodes();

        var pathExtractor = OptimalPathExtractor.of(dependencies);
        var generator = DotFileGenerator.init(MAX_DEPTH_IN_PLOT);
        var nodes = pathExtractor.extract(root).getNodes();
        var text = generator.generateDot(tree, nodes);
        var filePath = ProjectPropertiesReader.create().pathMcts();
        generator.writeToFile(filePath + FILE_NAME, text);

        nodes.forEach(System.out::println);

        Assertions.assertTrue(sizeTree > 5);
        Assertions.assertEquals(-3.0,nodes.get(nodes.size() - 1).info().state().y(),0.5);
    }



}
