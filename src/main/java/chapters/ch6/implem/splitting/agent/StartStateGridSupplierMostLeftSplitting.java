package chapters.ch6.implem.splitting.agent;

import chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath;
import core.gridrl.StartStateGridSupplierI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * A supplier of start states for the Splitting environment, specifically designed to provide the most left splitting state.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateGridSupplierMostLeftSplitting implements StartStateGridSupplierI {

    public static StartStateGridSupplierMostLeftSplitting create() {
        return new StartStateGridSupplierMostLeftSplitting();
    }

    @Override
    public String environmentName() {
        return EnvironmentSplittingPath.NAME;
    }

    @Override
    public StateGrid getStartState() {
        return StateGrid.of(0,1);
    }
}
