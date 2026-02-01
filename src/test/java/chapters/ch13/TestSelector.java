package chapters.ch13;

import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.searcher.workers.Selector;
import chapters.ch13.domain.tree.Tree;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.factory.FactorySearcherSettings;
import chapters.ch13.factory.FactoryTreeForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSelector {

    public static final double LEARNING_RATE = 1.0;
    Tree<StateJumper, ActionJumper> tree;
    Selector<StateJumper, ActionJumper> selector;

    @BeforeEach
    void init() {
        tree = FactoryTreeForTest.createClimbingTree();
        tree.getRoot().update(LEARNING_RATE,0);  //parent count must be > 0 for uct calculation
        selector = Selector.of(FactorySearcherSettings.forTestClimber());
    }

    @Test
    void childZeroHigherValue() {
        updateChild(0, 20);
        updateChild(1, 10);
        var bestChild = selector.selectFromTriedActions(tree.getRoot());
        Assertions.assertEquals(getChild(0), bestChild);
    }

    @Test
    void childOneHigherValue() {
        updateChild(0, 10);
        updateChild(1, 20);
        var bestChild = selector.selectFromTriedActions(tree.getRoot());
        Assertions.assertEquals(getChild(1), bestChild);
    }

    @Test
    void childsZeroCountsValues() {
        var bestChild = selector.selectFromTriedActions(tree.getRoot());
        Assertions.assertTrue(bestChild.equals(getChild(0)) ||
                bestChild.equals(getChild(1)));
    }


    @Test
    void childsSameValues() {
        updateChild(0, 10);
        updateChild(1, 10);
        var bestChild = selector.selectFromTriedActions(tree.getRoot());
        Assertions.assertTrue(bestChild.equals(getChild(0)) ||
                bestChild.equals(getChild(1)));
    }

    @Test
    void childsSameValuesChild0MoreCounts_thenSelect1() {
        updateChild(0, 0);
        updateChild(0, 0);
        var bestChild = selector.selectFromTriedActions(tree.getRoot());
        Assertions.assertTrue(bestChild.equals(getChild(1)));
    }


    private void updateChild(int index, double value) {
        getChild(index).update(LEARNING_RATE,value);
    }

    private Node<StateJumper, ActionJumper> getChild(int index) {
        return tree.getRoot().info().children().get(index);
    }


}
