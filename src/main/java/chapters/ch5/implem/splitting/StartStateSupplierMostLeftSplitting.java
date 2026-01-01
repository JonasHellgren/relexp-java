package chapters.ch5.implem.splitting;

import chapters.ch5.domain.environment.StartStateSupplierI;
import chapters.ch5.domain.environment.StateMcI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierMostLeftSplitting implements StartStateSupplierI {

    public static StartStateSupplierMostLeftSplitting create() {
        return new StartStateSupplierMostLeftSplitting();
    }

    @Override
    public String environmentName() {
        return EnvironmentSplittingMc.NAME;
    }

    @Override
    public StateMcI getStartState() {
        return StateSplittingMc.of(0,1);
    }
}
