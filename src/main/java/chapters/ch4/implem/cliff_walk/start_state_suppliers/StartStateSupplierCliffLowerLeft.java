package chapters.ch4.implem.cliff_walk.start_state_suppliers;

import chapters.ch4.domain.start_state_supplier.StartStateGridSupplierI;
import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * A StartStateSupplier implementation for the Cliff environment.
 * This implementation starts the agent at the lower left corner of the grid.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierCliffLowerLeft implements StartStateGridSupplierI {

    public static StartStateSupplierCliffLowerLeft create() {
        return new StartStateSupplierCliffLowerLeft();
    }

    @Override
    public String environmentName() {
        return EnvironmentCliff.NAME;
    }

    @Override
    public StateGrid getStartState() {
        return StateGrid.of(0, 0);
    }
}
