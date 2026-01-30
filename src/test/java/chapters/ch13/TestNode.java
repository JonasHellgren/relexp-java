package chapters.ch13;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.searcher.node.Node;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.EnvironmentJumper;
import chapters.ch13.implem.jumper.StateJumper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestNode {

    public static final double TOL = 0.0001;
    public static final double LEARNING_RATE = 1.0;
    private Node<StateJumper, ActionJumper> root, nodeChildUp, nodeChildDown;

    @BeforeEach
    public void setup() {
        root = Node.of("root", null, Experience.noTerm(null,null,0, StateJumper.zeroHeight()), ActionJumper.actions());
        var environment = EnvironmentJumper.create();
        var state = root.info().state();
        var actionUp = ActionJumper.up;
        var actionDown = ActionJumper.n;
        var expUp= Experience.of(state, actionUp, environment.step(state,actionUp));
        var expDown= Experience.of(state, actionDown, environment.step(state,actionDown));
        nodeChildUp = Node.of("chUp", root, expUp, ActionJumper.actions());
        nodeChildDown = Node.of("chDown", root, expDown, ActionJumper.actions());
    }

    @Test
    void testConstructor() {
        assertNotNull(root.info());
        assertNotNull(root.info().name());
        assertNotNull(root.info().state());
        assertNull(root.info().action());
        assertNull(root.info().parent());
        assertTrue(root.info().isRoot());
        assertNotNull(root.info().children());
    }

    @Test
    void testUpdate() {
        root.update(LEARNING_RATE,10.0);
        Assertions.assertEquals(10.0, root.info().value(), TOL);
        Assertions.assertEquals(1, root.info().count(), TOL);
    }

    @Test
    void testValue() {
        double singleReturn = 10.0;
        root.update(LEARNING_RATE,singleReturn);
        root.update(LEARNING_RATE,singleReturn);
        double mean = (singleReturn + singleReturn) / 2.0;
        Assertions.assertEquals(mean, root.info().value(), TOL);
        Assertions.assertEquals(2, root.info().count(), TOL);
    }

    @Test
    void testAddChild() {
        root.addChild(nodeChildUp);

        Assertions.assertEquals(1, root.info().nChildrens());
        Assertions.assertFalse(root.info().isAllActionsTried());
        assertTrue(root.info().isLeaf());
        Assertions.assertEquals(0, root.info().count());
        Assertions.assertEquals(0.0, root.info().value(), TOL);
        Assertions.assertEquals(0.0, root.info().reward(), TOL);
        assertTrue(root.info().isChildPresent(ActionJumper.up));
        Assertions.assertFalse(root.info().isChildPresent(ActionJumper.n));
        Assertions.assertEquals(0.0, root.info().valueChild(ActionJumper.up), TOL);
        Assertions.assertEquals(List.of(ActionJumper.n), root.info().nonTriedActions());
    }

    @Test
    void testAddTwoChildren() {
        root.addChild(nodeChildUp);
        root.addChild(nodeChildDown);
        Assertions.assertEquals(2, root.info().nChildrens());
        assertTrue(root.info().isAllActionsTried());
        Assertions.assertFalse(root.info().isLeaf());
        Assertions.assertEquals(List.of(), root.info().nonTriedActions());
    }


}
