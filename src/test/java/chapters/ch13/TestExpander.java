package chapters.ch13;

import chapters.ch13.domain.searcher.workers.Expander;
import chapters.ch13.domain.tree.Tree;
import chapters.ch13.factory.jumper.JumperParametersFactory;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.EnvironmentJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.factory.FactoryNameFunction;
import chapters.ch13.factory.FactoryTreeForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestExpander {


    Expander<StateJumper, ActionJumper> expander;
    Tree<StateJumper, ActionJumper> tree;

    @BeforeEach
    void init() {
        var environment = EnvironmentJumper.of(JumperParametersFactory.produce());
        tree = FactoryTreeForTest.createClimbingTree();
        var nameFunction = FactoryNameFunction.rand3DigitClimber;
        expander = Expander.of(environment, nameFunction);
    }

    @Test
    void whenExpandThenCorrect() {
        var leafNode = tree.getRoot().info().children().get(0);
        int nChildsLeafBefore = leafNode.info().children().size();
        int nNOdesBefore = tree.info().numberOfNodes();
        var newNode = expander.expand(leafNode);
        int nChildsLeafAfter = leafNode.info().children().size();
        int nNOdesAfter = tree.info().numberOfNodes();

        Assertions.assertEquals(nChildsLeafBefore + 1, nChildsLeafAfter);
        Assertions.assertEquals(leafNode, newNode.info().parent());
        Assertions.assertTrue(newNode.info().isLeaf());
        ActionJumper actionNew = newNode.info().action();
        Assertions.assertTrue(actionNew.equals(ActionJumper.up) || actionNew.equals(ActionJumper.n));
        Assertions.assertEquals(nNOdesBefore + 1, nNOdesAfter);
    }


}
