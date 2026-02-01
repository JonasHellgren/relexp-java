package chapters.ch13;

import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.searcher.path.Path;
import chapters.ch13.domain.searcher.workers.Simulator;
import chapters.ch13.domain.tree.Tree;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.EnvironmentJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.factory.FactoryRolloutPolicy;
import chapters.ch13.factory.FactorySearcherSettings;
import chapters.ch13.factory.FactoryTreeForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

public class TestSimulator {

    Tree<StateJumper, ActionJumper> tree;
    Simulator<StateJumper, ActionJumper> simulator;

    @BeforeEach
    void init() {
        tree = FactoryTreeForTest.createClimbingTree();
        var environment = EnvironmentJumper.create();
        var policy = FactoryRolloutPolicy.rolloutPolicyClimb;
        var settings = FactorySearcherSettings.forTestClimber();
        simulator = Simulator.of(environment, policy, settings);
    }

    /**Simulation can end up in different paths, one is a=down (direct terminal) and
     * other is a=up (not direct terminal in simulation)
     */

    @Test
    void testOneSimulation() {
        var startNode = tree.getRoot().info().children().get(0);
        var path = getPath(startNode);

        int lengthBefore = path.info().length();
        var pathNew = simulator.simulate(startNode, path);
        int lengthAfter = pathNew.info().length();

        Assertions.assertEquals(2, lengthBefore);
        Assertions.assertTrue(lengthAfter==3 || lengthAfter==4);
    }

    @Test
    void testManySimulations() {
        var startNode = tree.getRoot().info().children().get(0);
        Set<Double> returnsRoot = new HashSet<>();
        for (int i = 0; i < 100 ; i++) {
            var path = getPath(startNode);
            var pathNew = simulator.simulate(startNode, path);
            returnsRoot.add(pathNew.info().returnForPosInPath(0, 1.0));
        }
        Assertions.assertTrue(returnsRoot.size()>2);
    }

    private Path<StateJumper, ActionJumper> getPath(Node<StateJumper, ActionJumper> startNode) {
        Path<StateJumper, ActionJumper> path = Path.init();
        path.addNode(tree.getRoot());
        path.addNode(startNode);
        return path;
    }
}