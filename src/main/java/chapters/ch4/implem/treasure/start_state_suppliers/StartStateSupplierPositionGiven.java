package chapters.ch4.implem.treasure.start_state_suppliers;

import core.gridrl.StartStateGridSupplierI;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * A class that implements the StartStateGridSupplierI interface to provide start states for
 * the Treasure Hunt environment.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierPositionGiven implements StartStateGridSupplierI {

    StateGrid state;

    public static StartStateSupplierPositionGiven create(StateGrid state) {
        return new StartStateSupplierPositionGiven(state);
    }

    @Override
    public String environmentName() {
        return EnvironmentTreasure.NAME;
    }

    @Override
    public StateGrid getStartState() {
            return state;
    }
}
