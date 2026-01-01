package chapters.ch5.implem.splitting;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplittingFactory;
import chapters.ch5.domain.environment.StartStateSupplierI;
import chapters.ch5.domain.environment.StateMcI;
import core.foundation.gadget.set.SetUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierRandomSplitting implements StartStateSupplierI {

    public static StartStateSupplierRandomSplitting create() {
        return new StartStateSupplierRandomSplitting();
    }

    @Override
    public String environmentName() {
        return EnvironmentSplittingMc.NAME;
    }

    @Override
    public StateMcI getStartState() {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var stateGrid =   SetUtils.getAnyFromSet(parameters.getAllStates());
        return SplittingPathAdapter.getStateGridMc(stateGrid);
    }

}
