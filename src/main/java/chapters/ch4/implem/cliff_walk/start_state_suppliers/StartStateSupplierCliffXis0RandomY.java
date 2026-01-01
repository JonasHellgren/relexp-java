package chapters.ch4.implem.cliff_walk.start_state_suppliers;

import chapters.ch4.domain.start_state_supplier.StartStateGridSupplierI;
import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import core.foundation.util.rand.RandUtils;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * A supplier of start states for the Cliff Walk environment, where the x-position is
 * fixed at 0 and the y-position is random.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierCliffXis0RandomY implements StartStateGridSupplierI {

    private final EnvironmentCliff environment;

    public static StartStateSupplierCliffXis0RandomY of(EnvironmentCliff environment) {
        return new StartStateSupplierCliffXis0RandomY(environment);
    }

    @Override
    public String environmentName() {
        return EnvironmentCliff.NAME;
    }

    @Override
    public StateGrid getStartState() {
        var parameters = environment.getParameters();
        var yMinMax = parameters.getPosYMinMax();
        int y = RandUtils.getRandomIntNumber(yMinMax.getFirst(), yMinMax.getSecond() - 1);
        return StateGrid.of(0, y);
    }
}
