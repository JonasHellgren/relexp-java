package ch13;

import chapters.ch13.domain.searcher.node.Node;
import chapters.ch13.environments.jumper.ActionJumper;
import chapters.ch13.environments.jumper.StateJumper;
import chapters.ch13.factory.FactoryTreeForTest;
import chapters.ch13.plotting.DotFileGenerator;
import core.foundation.configOld.ProjectPropertiesReader;
import lombok.SneakyThrows;

import java.util.List;

/**
 * dot -Tpng pictures/k_mcts/climb_tiny_mcts.dot -o pictures/k_mcts/climb_tiny_mcts.png
 */

public class RunnerDotFileGenerator {

    public static final String FILE_NAME = "climb_tiny_mcts.dot";

    @SneakyThrows
    public static void main(String[] args) {
        var tree= FactoryTreeForTest.createClimbingTree();
        var generator = DotFileGenerator.init();
        List<Node<StateJumper, ActionJumper>> bestPath=List.of(tree.getRoot().info().children().get(0));
        var text=generator.generateDot(tree, bestPath);

        var path = ProjectPropertiesReader.create().pathMcts();
        generator.writeToFile(path+ FILE_NAME, text);

    }
}
