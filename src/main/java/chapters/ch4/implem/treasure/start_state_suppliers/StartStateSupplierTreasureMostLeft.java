package chapters.ch4.implem.treasure.start_state_suppliers;

import chapters.ch4.domain.start_state_supplier.StartStateGridSupplierI;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * A class that implements the StartStateGridSupplierI interface to provide start states for
 * the Treasure Hunt environment.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierTreasureMostLeft implements StartStateGridSupplierI {

    public static StartStateSupplierTreasureMostLeft create() {
        return new StartStateSupplierTreasureMostLeft();
    }

    @Override
    public String environmentName() {
        return EnvironmentTreasure.NAME;
    }

    @Override
    public StateGrid getStartState() {
        return StateGrid.of(0, 1);
    }
}
