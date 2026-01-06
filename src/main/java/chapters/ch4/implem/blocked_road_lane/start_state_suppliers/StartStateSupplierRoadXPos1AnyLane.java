package chapters.ch4.implem.blocked_road_lane.start_state_suppliers;

import core.gridrl.StartStateGridSupplierI;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import core.foundation.util.rand.RandUtils;
import core.gridrl.StateGrid;

import java.util.List;

/**
 * A supplier of start states for the Road environment, where the x-position is 0 and the lane is random.
 */
public class StartStateSupplierRoadXPos1AnyLane implements StartStateGridSupplierI {

    public static StartStateSupplierRoadXPos1AnyLane create() {
        return new StartStateSupplierRoadXPos1AnyLane();
    }

    @Override
    public String environmentName() {
        return EnvironmentRoad.NAME;
    }

    @Override
    public StateGrid getStartState() {
        var validStartStates = List.of(StateGrid.of(1, 0), StateGrid.of(1, 1));
        return validStartStates.get(RandUtils.getRandomIntNumber(0, validStartStates.size()));
    }
}
