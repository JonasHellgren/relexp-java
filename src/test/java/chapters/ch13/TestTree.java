package chapters.ch13;

import chapters.ch13.domain.searcher.tree.Tree;
import chapters.ch13.environments.jumper.ActionJumper;
import chapters.ch13.environments.jumper.StateJumper;
import chapters.ch13.factory.FactoryTreeForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTree {

    Tree<StateJumper, ActionJumper> tree;

    @BeforeEach
    void init() {
        tree = FactoryTreeForTest.createClimbingTree();
    }

    @Test
    void testTreeProperties() {
        Assertions.assertEquals(3, tree.info().numberOfNodes());
        Assertions.assertEquals(2, tree.info().depth());
    }


}
