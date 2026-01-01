package chapters.ch5.implem.dice;


import chapters.ch5.domain.environment.StartStateSupplierI;
import chapters.ch5.domain.environment.StateMcI;

public class StartStateSupplierDice implements StartStateSupplierI {

    public static StartStateSupplierDice create() {
        return new StartStateSupplierDice();
    }

    @Override
    public String environmentName() {
        return EnvironmentDice.NAME;
    }

    @Override
    public StateMcI getStartState() {
        return StateDice.start();
    }
}
