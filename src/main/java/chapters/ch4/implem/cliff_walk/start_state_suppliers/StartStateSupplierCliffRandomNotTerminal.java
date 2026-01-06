package chapters.ch4.implem.cliff_walk.start_state_suppliers;

import chapters.ch4.domain.start_state_supplier.StartStateGridSupplierI;
import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import core.foundation.util.rand.RandUtils;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * A supplier of start states for the Cliff Walk environment, where the start state is chosen randomly
 * but not in a terminal state.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierCliffRandomNotTerminal implements StartStateGridSupplierI {

    private final EnvironmentCliff environment;

    public static StartStateSupplierCliffRandomNotTerminal of(EnvironmentCliff environment) {
        return new StartStateSupplierCliffRandomNotTerminal(environment);
    }

    @Override
    public String environmentName() {
        return EnvironmentCliff.NAME;
    }

    @Override
    public StateGrid getStartState() {
        var state=StateGrid.of(0,0);
        var informer = environment.getInformer();
        do {
            var xMinMax = informer.getPosXMinMax();
            var yMinMax = informer.getPosYMinMax();
            int x = RandUtils.getRandomIntNumber(xMinMax.getFirst(), xMinMax.getSecond() - 1);
            int y = RandUtils.getRandomIntNumber(yMinMax.getFirst(), yMinMax.getSecond() - 1);
            state=StateGrid.of(x,y);
        }while (informer.isTerminal(state));
        return state;
    }
}
