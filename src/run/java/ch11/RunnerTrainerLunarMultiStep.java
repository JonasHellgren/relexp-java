package ch11;

import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierI;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierRandomAndClipped;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.core.TrainerLunarMultiStep;
import chapters.ch11.factory.LunarEnvParamsFactory;
import core.foundation.gadget.timer.CpuTimer;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.math3.util.Pair;
import org.jetbrains.annotations.NotNull;

import static ch11.RunnerHelper.*;

@Log
public class RunnerTrainerLunarMultiStep {

    public static final int STEP_HORIZON = 5;
    public static final int N_EPISODES = 10_000;
    public static final int N_EVALS = 100;

    @SneakyThrows
    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        var ep = LunarEnvParamsFactory.produceDefault();
        var trainerDependencies = getDependencies(ep, STEP_HORIZON, N_EPISODES);
        var trainer = TrainerLunarMultiStep.of(trainerDependencies);
        trainer.train();
        timer.printInMs();
        plot(trainer, trainerDependencies);
        evaluate(trainerDependencies, getStartStateEvaluation(ep), N_EVALS);
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

}
