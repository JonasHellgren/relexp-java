package chapters.ch8;

import chapters.ch8.domain.agent.core.AgentParking;
import chapters.ch8.domain.agent.param.AgentParkingParameters;
import chapters.ch8.domain.environment.core.EnvironmentParking;
import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.param.ParkingParameters;
import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplier;
import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch8.domain.trainer.core.TrainerDependenciesParking;
import chapters.ch8.domain.trainer.core.TrainerParking;
import chapters.ch8.domain.trainer.param.TrainerParametersParking;
import chapters.ch8.factory.AgentParkingParametersFactory;
import chapters.ch8.factory.ParkingParametersFactory;
import chapters.ch8.factory.TrainerParametersFactory;
import core.foundation.gadget.math.LogarithmicDecay;
import org.apache.commons.math3.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import static chapters.ch8.factory.TrainerDepFactory.getTrainerDependenciesParking;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCorrectPolicyWhenTrained {

    public static final double FEE_CHARGING2 = 2;  //shall give one rejection at (nOccup, fee)=(4,2)
    public static final double FEE_CHARGING3 = 3;  //shall give two rejection at (nOccup, fee)=(4,3), (3,3)
    public static AgentParking agentFee2,agentFee3;

    @BeforeAll
    static void init() {
        var dependencies2 = getDependenciesParking(FEE_CHARGING2);
        var trainer2 = TrainerParking.of(dependencies2);
        trainer2.train();
        agentFee2 = dependencies2.agent();

        var dependencies3 = getDependenciesParking(FEE_CHARGING3);
        var trainer3 = TrainerParking.of(dependencies3);
        trainer3.train();
        agentFee3 = dependencies3.agent();

    }

    @ParameterizedTest
    @CsvSource({
            "1, NotCharging, A",  //nOccup, fee  ->  action
            "2, NotCharging, A",
            "3, NotCharging, A",
            "4, NotCharging, R",
            "1, Charging, A",
            "2, Charging, A",
            "3, Charging, A",
            "4, Charging, A",
    })
    void givenFee2_whenTrained_thenCorrectActions(ArgumentsAccessor accessor) {
        var s = getStateParking(accessor);
        var action = agentFee2.chooseActionNoExploration(s).getLetter();
        assertEquals(accessor.getString(2), action);
    }


    @ParameterizedTest
    @CsvSource({
            "1, NotCharging, A",  //nOccup, fee  ->  action
            "2, NotCharging, A",
            "3, NotCharging, R",
            "4, NotCharging, R",
            "1, Charging, A",
            "2, Charging, A",
            "3, Charging, A",
            "4, Charging, A",
    })
    void givenFee3_whenTrained_thenCorrectActions(ArgumentsAccessor accessor) {
        var s = getStateParking(accessor);
        var action = agentFee3.chooseActionNoExploration(s).getLetter();
        assertEquals(accessor.getString(2), action);
    }

    private static StateParking getStateParking(ArgumentsAccessor accessor) {
        return StateParking.ofStart(accessor.getInteger(0), FeeEnum.valueOf(accessor.getString(1)));
    }

    private static TrainerDependenciesParking getDependenciesParking(double feeCharging) {
        var agentPar = AgentParkingParametersFactory.forRunning();
        var envPar = ParkingParametersFactory.forRunning().withFeeCharging(feeCharging);
        var tp = TrainerParametersFactory.forRunning();
        var startSup = StartStateSupplier.ZEROOCCUP_RANDOMFEE.of(envPar);
        return getTrainerDependenciesParking(agentPar, envPar, tp, startSup);
    }



}
