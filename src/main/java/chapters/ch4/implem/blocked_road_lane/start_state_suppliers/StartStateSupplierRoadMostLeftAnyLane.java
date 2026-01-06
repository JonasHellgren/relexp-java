package chapters.ch4.implem.blocked_road_lane.start_state_suppliers;


import core.gridrl.StartStateGridSupplierI;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import core.foundation.util.rand.RandUtils;
import core.gridrl.StateGrid;

import java.util.List;

/**
 * A supplier of start states for the Road environment, where the agent starts at the most left position.
 */
public class StartStateSupplierRoadMostLeftAnyLane implements StartStateGridSupplierI {

    public static StartStateSupplierRoadMostLeftAnyLane create() {
        return new StartStateSupplierRoadMostLeftAnyLane();
    }

    @Override
    public String environmentName() {
        return EnvironmentRoad.NAME;
    }

    @Override
    public StateGrid getStartState() {
        var validStartStates = List.of(StateGrid.of(0, 0), StateGrid.of(0, 1));
        return validStartStates.get(RandUtils.getRandomIntNumber(0, validStartStates.size()));
    }
}
