package chapters.ch4.implem.treasure.start_state_suppliers;

import core.gridrl.StartStateGridSupplierI;
import chapters.ch4.implem.treasure.core.EnvironmentParametersTreasure;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import chapters.ch4.implem.treasure.core.InformerTreasure;
import core.foundation.util.rand.RandUtil;
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
        var informer= InformerTreasure.create(parameters);
        var posXMinMax= informer.getPosXMinMax();
        var posYMinMax= informer.getPosYMinMax();

        boolean isNotValid = true;
        int x = 0;
        int y = 0;
        do {
            x = RandUtil.getRandomIntNumber(posXMinMax.getFirst(), posXMinMax.getSecond() - 1);
            y = RandUtil.getRandomIntNumber(posYMinMax.getFirst(), posYMinMax.getSecond() - 1);
            StateGrid state = StateGrid.of(x, y);
            isNotValid = informer.isTerminalNonFail(state) || informer.isWall(state) || informer.isFail(state);
        } while (isNotValid);

        return StateGrid.of(x,y);
    }
}
