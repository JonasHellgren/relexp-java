package chapters.ch13;

import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.searcher.path.OptimalPathExtractor;
import chapters.ch13.domain.searcher.core.OuterDependencies;
import chapters.ch13.domain.searcher.core.Searcher;
import chapters.ch13.factory.jumper.FactoryDependenciesJumper;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.factory.jumper.FactoryTreeJumper;
import chapters.ch13.plotting.DotFileGenerator;
import core.foundation.config.ConfigFactory;
import core.foundation.configOld.ProjectPropertiesReader;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/***
 *  dot -Tpng pictures/k_mcts/jumper_test_mcts.dot -o pictures/k_mcts/jumper_test_mcts.png
 */

public class TestSearcherJumper {

    OuterDependencies<StateJumper, ActionJumper> dependencies;
    Searcher<StateJumper, ActionJumper> searcher;
    Node<StateJumper, ActionJumper> root;
    public static final String FILE_NAME = "jumper_test_mcts.dot";


    @BeforeEach
    void init() {
        dependencies = FactoryDependenciesJumper.test();
        searcher = Searcher.of(dependencies);
        root = FactoryTreeJumper.onlyRoot();
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
        var path = ConfigFactory.pathPicsConfig().ch13();
        generator.writeToFile(path + FILE_NAME, text);

        Assertions.assertTrue(sizeTree > 5);
        Assertions.assertTrue(nodes.get(nodes.size() - 1).info().state().height() == 3);
    }


}
