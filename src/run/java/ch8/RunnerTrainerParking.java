package ch8;

import chapters.ch8.domain.agent.core.AgentParking;
import chapters.ch8.domain.environment.core.EnvironmentParking;
import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplier;
import chapters.ch8.domain.trainer.core.TrainerDependenciesParking;
import chapters.ch8.domain.trainer.core.TrainerParking;
import chapters.ch8.factory.AgentParkingParametersFactory;
import chapters.ch8.factory.ParkingParametersFactory;
import chapters.ch8.factory.TrainerParametersFactory;
import chapters.ch8.plotting.AgentParkingMemoryCurvePlotter;
import chapters.ch8.plotting.AgentParkingMemoryHeatMapPlotter;
import chapters.ch8.plotting.TrainerPlotter;
import core.foundation.gadget.math.LogarithmicDecay;
import core.foundation.gadget.timer.CpuTimer;
import org.apache.commons.math3.util.Pair;


public class RunnerTrainerParking {

    public static final double FEE_CHARGING = 2;  //2 or 3

    public static void main(String[] args) {
        var dependencies = getDependenciesParking();
        var trainer = TrainerParking.of(dependencies);
        trainer.train();
        trainer.logTimer();
        TrainerPlotter.plotTrainEvolution(trainer);
        var agentPlotter= AgentParkingMemoryHeatMapPlotter.of(dependencies);
        double feeNoCharging = dependencies.environment().getParameters().feeNoCharging();
        agentPlotter.plotMemory(Pair.create(feeNoCharging,FEE_CHARGING));
        var agentCurvePlotter= AgentParkingMemoryCurvePlotter.of(dependencies);
        agentCurvePlotter.plotMemory(FEE_CHARGING);
    }

    private static TrainerDependenciesParking getDependenciesParking() {
        var envPar= ParkingParametersFactory.forRunning().withFeeCharging(FEE_CHARGING);
        var tp= TrainerParametersFactory.forRunning();
        return TrainerDependenciesParking.builder()
                .agent(AgentParking.of(AgentParkingParametersFactory.forRunning()))
                .environment(EnvironmentParking.of(envPar))
                .trainerParameters(tp)
                .startStateSupplier(StartStateSupplier.ZEROOCCUP_RANDOMFEE.of(envPar))
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
