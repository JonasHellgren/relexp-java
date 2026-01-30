package chapters.ch13.factory;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.searcher.node.Node;
import chapters.ch13.domain.searcher.tree.Tree;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.EnvironmentJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.StateLane;
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
