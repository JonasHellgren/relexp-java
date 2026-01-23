package ch8;

import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplier;
import chapters.ch8.domain.trainer.core.TrainerDependenciesParking;
import chapters.ch8.domain.trainer.core.TrainerParking;
import chapters.ch8.factory.AgentParkingParametersFactory;
import chapters.ch8.factory.ParkingParametersFactory;
import chapters.ch8.factory.TrainerParametersFactory;
import chapters.ch8.plotting.AgentParkingMemoryCurvePlotter;
import chapters.ch8.plotting.AgentParkingMemoryHeatMapPlotter;
import chapters.ch8.plotting.TrainerPlotter;
import core.foundation.config.ConfigFactory;
import org.apache.commons.math3.util.Pair;
import static chapters.ch8.factory.TrainerDepFactory.getTrainerDependenciesParking;

public class RunnerTrainerParking {

    public static final double FEE_CHARGING = 2;  //2 or 3
    public static final String FILE_NAME_VALUES = "park_values_curve_fee";

    public static void main(String[] args) {
        var dependencies = getDependenciesParking();
        var trainer = TrainerParking.of(dependencies);
        trainer.train();
        trainer.logTimer();
        plotting(trainer, dependencies);
    }

    private static void plotting(TrainerParking trainer, TrainerDependenciesParking dependencies) {
        var path = ConfigFactory.pathPicsConfig().ch8();
        var plotCfg=ConfigFactory.plotConfig();
        TrainerPlotter.plotTrainEvolution(path,trainer);
        var agentPlotter = AgentParkingMemoryHeatMapPlotter.of(dependencies,path,plotCfg);
        double feeNoCharging = dependencies.environment().getParameters().feeNoCharging();
        agentPlotter.plotMemory(Pair.create(feeNoCharging, FEE_CHARGING));
        var agentCurvePlotter = AgentParkingMemoryCurvePlotter.of(dependencies);
        agentCurvePlotter.saveAndShow(path, FILE_NAME_VALUES +FEE_CHARGING,plotCfg);
    }

    private static TrainerDependenciesParking getDependenciesParking() {
        var envPar = ParkingParametersFactory.forRunning().withFeeCharging(FEE_CHARGING);
        var tp = TrainerParametersFactory.forRunning();
        var agentPar = AgentParkingParametersFactory.forRunning();
        var startSup = StartStateSupplier.ZEROOCCUP_RANDOMFEE.of(envPar);
        return getTrainerDependenciesParking(agentPar, envPar, tp, startSup);
    }

}
