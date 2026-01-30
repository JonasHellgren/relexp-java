package ch11;

import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierI;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierRandomAndClipped;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.core.TrainerLunarMultiStep;
import chapters.ch11.factory.DependencyFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import chapters.ch11.plotting.AgentEvaluator;
import chapters.ch11.plotting.LunarTrainerPlotter;
import core.foundation.config.ConfigFactory;
import core.foundation.config.PathAndFile;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;

@Log
public class RunnerTrainerLunarMultiStep {

    public static final int STEP_HORIZON = 5;
    public static final int N_EPISODES = 10_000;
    public static final int N_EVALS = 100;
    static final String PATH = ConfigFactory.pathPicsConfig().ch11();

    @SneakyThrows
    public static void main(String[] args) {
        var ep = LunarEnvParamsFactory.produceDefault();
        var trainerDependencies = DependencyFactory.produce(ep, STEP_HORIZON, N_EPISODES);
        var trainer = TrainerLunarMultiStep.of(trainerDependencies);
        trainer.train();
        trainer.logTimer();
        plotting(trainer, trainerDependencies, ep);
    }

    private static void plotting(TrainerLunarMultiStep trainer,
                                 TrainerDependencies trainerDependencies,
                                 LunarParameters ep) {
        LunarTrainerPlotter.plot(trainer, trainerDependencies,PATH);
        var evaluator=AgentEvaluator.of(trainerDependencies, getStartStateEvaluation(ep));
        double fractFails=evaluator.fractionFails(N_EVALS);
        log.info("frac fails: " + fractFails);
        testSimulations(trainerDependencies);
    }

    @NotNull
    private static StartStateSupplierI getStartStateEvaluation(LunarParameters ep) {
        return StartStateSupplierRandomAndClipped.create(
                ep,Pair.create(ep.yMax(), ep.yMax()), Pair.create(-ep.spdMax(), -0d));
    }

    private static void testSimulations(TrainerDependencies trainerDependencies)  {
        simulateTrainedAgent(trainerDependencies, 2d, 1d, "lunar_simulationSpdPlus1");
        simulateTrainedAgent(trainerDependencies, 5d, -1d, "lunar_simulationSpd1");
        simulateTrainedAgent(trainerDependencies, 5.0d, -4d, "lunar_simulationSpd4");
    }

    private static void simulateTrainedAgent(TrainerDependencies trainerDependencies,
                                     double startHeight,
                                     double startSpd,
                                     String fileName) {
        var ep = trainerDependencies.environment().getParameters();
        trainerDependencies = trainerDependencies.withStartStateSupplier(new StartStateSupplierRandomAndClipped(
                ep, Pair.create(startHeight, startHeight), Pair.create(startSpd, startSpd)));
        var evaluatorSim = AgentEvaluator.of(trainerDependencies);
        var pathAndFile= PathAndFile.of(PATH, fileName);
        var plotConfig=ConfigFactory.plotConfig();
        evaluatorSim.plotAndSavePicFromSimulation(pathAndFile, plotConfig);
    }

}
