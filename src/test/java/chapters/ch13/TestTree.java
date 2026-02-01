package chapters.ch13;

import chapters.ch13.domain.tree.Tree;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.factory.jumper.FactoryTreeJumper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTree {

    Tree<StateJumper, ActionJumper> tree;

    @BeforeEach
    void init() {
        tree = FactoryTreeJumper.tinyTree();
    }

    @Test
    void testTreeProperties() {
        Assertions.assertEquals(3, tree.info().numberOfNodes());
        Assertions.assertEquals(2, tree.info().depth());
    }


}
