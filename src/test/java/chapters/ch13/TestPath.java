package chapters.ch13;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.searcher.path.Path;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.EnvironmentJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.factory.FactoryTreeForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * (root) -> (newNode) -> (simExp0) -> (simExp1)
 */

public class TestPath {

    public static final ActionJumper ACTION = ActionJumper.up;
    public static final double DISCOUNT = 1.0;
    Path<StateJumper, ActionJumper> path;

    @BeforeEach
    void init() {
        var tree = FactoryTreeForTest.createClimbingTree();
        var newNode = tree.getRoot().info().children().get(0);
        var treeNodes = List.of(tree.getRoot(), newNode);

        List<Experience<StateJumper, ActionJumper>> simulation = new ArrayList<>();
        var environment = EnvironmentJumper.create();
        var stepReturn0 = environment.step(newNode.info().state(), ACTION);
        simulation.add(Experience.of(newNode.info().state(), ACTION, stepReturn0));
        var stepReturn1 = environment.step(stepReturn0.stateNew(), ACTION);
        simulation.add(Experience.of(newNode.info().state(), ACTION, stepReturn1));
        path = Path.of(treeNodes, simulation);
    }

    @Test
    void correctPath() {

        System.out.println("path = " + path);

        var info = path.info();
        Assertions.assertEquals(4, info.length());
        Assertions.assertEquals(2, info.lengthNodesSimulation());
        Assertions.assertEquals(4, info.rewardsTreeAndSimulation().size());
        Assertions.assertEquals(3, info.returnForPosInPath(0, DISCOUNT));
        Assertions.assertFalse(info.isAnyTerminalInTree());
        Assertions.assertTrue(info.isAnyTerminalInSimulation());
    }


}
