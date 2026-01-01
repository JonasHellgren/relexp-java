package chapters.ch4.implem.treasure.start_state_suppliers;

import chapters.ch4.domain.start_state_supplier.StartStateGridSupplierI;
import chapters.ch4.implem.treasure.core.EnvironmentParametersTreasure;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import core.foundation.util.rand.RandUtils;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
/**
 * A class that implements the StartStateGridSupplierI interface to provide start states for
 * the Treasure Hunt environment.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierRandom implements StartStateGridSupplierI {

    EnvironmentParametersTreasure parameters;

    public static StartStateSupplierRandom create(EnvironmentParametersTreasure parameters) {
        return new StartStateSupplierRandom(parameters);
    }

    @Override
    public String environmentName() {
        return EnvironmentTreasure.NAME;
    }

    @Override
    public StateGrid getStartState() {
        var posXMinMax= parameters.getPosXMinMax();
        var posYMinMax= parameters.getPosYMinMax();

        boolean isNotValid = true;
        int x = 0;
        int y = 0;
        do {
            x = RandUtils.getRandomIntNumber(posXMinMax.getFirst(), posXMinMax.getSecond() - 1);
            y = RandUtils.getRandomIntNumber(posYMinMax.getFirst(), posYMinMax.getSecond() - 1);
            StateGrid state = StateGrid.of(x, y);
            isNotValid = parameters.isTerminalNonFail(state) || parameters.isWall(state) || parameters.isFail(state);
        } while (isNotValid);

        return StateGrid.of(x,y);
    }
}
