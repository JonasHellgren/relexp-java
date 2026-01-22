package chapters.ch8.factory;

import chapters.ch8.domain.agent.core.AgentParking;
import chapters.ch8.domain.agent.param.AgentParkingParameters;
import chapters.ch8.domain.environment.core.EnvironmentParking;
import chapters.ch8.domain.environment.param.ParkingParameters;
import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch8.domain.trainer.core.TrainerDependenciesParking;
import chapters.ch8.domain.trainer.param.TrainerParametersParking;
import core.foundation.gadget.math.LogarithmicDecay;
import core.foundation.gadget.timer.CpuTimer;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

@UtilityClass
public class TrainerDepFactory {



    public static TrainerDependenciesParking getTrainerDependenciesParking(AgentParkingParameters agentPar,
                                                                            ParkingParameters envPar,
                                                                            TrainerParametersParking tp,
                                                                            StartStateSupplierI startSup) {
        return TrainerDependenciesParking.builder()
                .agent(AgentParking.of(agentPar))
                .environment(EnvironmentParking.of(envPar))
                .trainerParameters(tp)
                .startStateSupplier(startSup)
                .timer(CpuTimer.empty())
                .probRandomDecay(decaying(tp.probRandomActionStartEnd(), envPar.maxSteps()))
                .learningRateDecay(decaying(tp.learningRateActionValueStartEnd(), envPar.maxSteps()))
                .learningRateRewardDecay(decaying(tp.learningRateRewardAverageStartEnd(), envPar.maxSteps()))
                .build();
    }



    private static LogarithmicDecay decaying(Pair<Double, Double> minMaxPar, int maxSteps) {
        return LogarithmicDecay.of(minMaxPar, maxSteps);
    }


}
