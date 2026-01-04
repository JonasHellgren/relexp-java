package chapters.ch13.factory;

import k_mcts.domain.environment.Experience;
import k_mcts.domain.searcher.node.Node;
import k_mcts.domain.searcher.tree.Tree;
import k_mcts.environments.jumper.ActionJumper;
import k_mcts.environments.jumper.EnvironmentJumper;
import k_mcts.environments.jumper.StateJumper;
import k_mcts.environments.lane_change.ActionLane;
import k_mcts.environments.lane_change.StateLane;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryTreeForTest {

    public static Tree<StateJumper, ActionJumper> createClimbingTree() {
        var root = getRootJumper();
        var environment = EnvironmentJumper.create();
        var state = root.info().state();
        var actionUp = ActionJumper.up;
        var actionDown = ActionJumper.n;
        var expUp= Experience.of(state, actionUp, environment.step(state,actionUp));
        var expDown= Experience.of(state, actionDown, environment.step(state,actionDown));
        var nodeChildUp = Node.of("1_0", root, expUp, ActionJumper.actions());
        var nodeChildDown = Node.of("1_1", root, expDown, ActionJumper.actions());
        root.addChild(nodeChildUp);
        root.addChild(nodeChildDown);
        return Tree.of(root);
    }

    public static Node<StateJumper, ActionJumper> getRootJumper() {
        return Node.of("root",
                null,
                Experience.noTerm(null,null,0, StateJumper.zeroHeight()),
                ActionJumper.actions());
    }

    public static Node<StateLane, ActionLane> getRootLane() {
        return Node.of("root",
                null,
                Experience.noTerm(null,null,0, StateLane.of(0, 0, 0, 0)),
                ActionLane.actions());
    }

}
