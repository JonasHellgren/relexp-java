package chapters.ch2.implem.splitting_path_problem;

import core.gridrl.StartStateSupplierGridI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierGridMostLeftSplitting implements StartStateSupplierGridI {

    public static StartStateSupplierGridMostLeftSplitting create() {
        return new StartStateSupplierGridMostLeftSplitting();
    }

    @Override
    public String environmentName() {
        return "SplittingPath";
    }

    @Override
    public StateGrid getStartState() {
        return StateGrid.of(0,1);
    }
}
