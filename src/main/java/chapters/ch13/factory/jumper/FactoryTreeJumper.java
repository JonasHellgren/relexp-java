package chapters.ch13.factory.jumper;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.tree.Node;
import chapters.ch13.domain.tree.Tree;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.EnvironmentJumper;
import chapters.ch13.implem.jumper.StateJumper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryTreeJumper {

    public static Tree<StateJumper, ActionJumper> tinyTree() {
        var root = onlyRoot();
        var environment = EnvironmentJumper.of(JumperParametersFactory.produce());
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

    public static Node<StateJumper, ActionJumper> onlyRoot() {
        return Node.of("root",
                null,
                Experience.noTerm(null,null,0, StateJumper.zeroHeight()),
                ActionJumper.actions());
    }


}
