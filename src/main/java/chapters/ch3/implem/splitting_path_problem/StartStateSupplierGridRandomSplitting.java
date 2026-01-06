package chapters.ch3.implem.splitting_path_problem;

import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import core.foundation.gadget.set.SetUtils;
import core.gridrl.StartStateSupplierGridI;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierGridRandomSplitting implements StartStateSupplierGridI {

    public static StartStateSupplierGridRandomSplitting create() {
        return new StartStateSupplierGridRandomSplitting();
    }

    @Override
    public String environmentName() {
        return "SplittingPath";
    }

    @Override
    public StateGrid getStartState() {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var informer= InformerSplitting.create(parameters);
        return   SetUtils.getAnyFromSet(informer.getAllStates());
    }
}
