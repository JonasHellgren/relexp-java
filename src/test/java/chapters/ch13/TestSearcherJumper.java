package chapters.ch13;

import chapters.ch13.domain.searcher.node.Node;
import chapters.ch13.domain.searcher.path.OptimalPathExtractor;
import chapters.ch13.domain.searcher.searcher.Dependencies;
import chapters.ch13.domain.searcher.searcher.Searcher;
import chapters.ch13.environments.jumper.ActionJumper;
import chapters.ch13.environments.jumper.StateJumper;
import chapters.ch13.factory.FactoryDependencies;
import chapters.ch13.factory.FactoryTreeForTest;
import chapters.ch13.plotting.DotFileGenerator;
import core.foundation.config.ProjectPropertiesReader;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/***
 *  dot -Tpng pictures/k_mcts/jumper_test_mcts.dot -o pictures/k_mcts/jumper_test_mcts.png
 */

public class TestSearcherJumper {

    Dependencies<StateJumper, ActionJumper> dependencies;
    Searcher<StateJumper, ActionJumper> searcher;
    Node<StateJumper, ActionJumper> root;
    public static final String FILE_NAME = "jumper_test_mcts.dot";


    @BeforeEach
    void init() {
        dependencies = FactoryDependencies.climberTest();
        searcher = Searcher.of(dependencies);
        root = FactoryTreeForTest.getRootJumper();
    }

    @SneakyThrows
    @Test
    void whenSearchThenCorrect() {
        var tree = searcher.search(root);
        int sizeTree = tree.info().numberOfNodes();

        var pathExtractor = OptimalPathExtractor.of(dependencies);
        var generator = DotFileGenerator.init();
        var nodes = pathExtractor.extract(root).getNodes();
        var text = generator.generateDot(tree, nodes);
        var path = ProjectPropertiesReader.create().pathMcts();
        generator.writeToFile(path + FILE_NAME, text);

        Assertions.assertTrue(sizeTree > 5);
        Assertions.assertTrue(nodes.get(nodes.size() - 1).info().state().height() == 3);
    }


}
