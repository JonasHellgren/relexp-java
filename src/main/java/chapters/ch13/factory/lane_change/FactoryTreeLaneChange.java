package chapters.ch13.factory.lane_change;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.domain.tree.Node;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.StateLane;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryTreeLaneChange {


    public static Node<StateLane, ActionLane> onlyRoot() {
        return Node.of("root",
                null,
                Experience.noTerm(null,null,0, StateLane.of(0, 0, 0, 0)),
                ActionLane.actions());
    }

}
