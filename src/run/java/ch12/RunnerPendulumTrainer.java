package ch12;


import chapters.ch12.inv_pendulum.domain.agent.evaluator.PendulumAgentEvaluator;
import chapters.ch12.inv_pendulum.domain.environment.startstate_supplier.StartStateSupplierEnum;
import chapters.ch12.inv_pendulum.domain.trainer.core.TrainerDependencies;
import chapters.ch12.inv_pendulum.domain.trainer.core.TrainerPendulum;
import chapters.ch12.inv_pendulum.factory.HyperParametersPendulum;
import chapters.ch12.inv_pendulum.factory.TrainerDependenciesFactory;
import chapters.ch12.inv_pendulum.plotting.PendulumAgentMemoryPlotter;
import core.foundation.gadget.timer.CpuTimer;

import static chapters.ch12.inv_pendulum.plotting.TrainerPlotter.*;

public class RunnerPendulumTrainer {
    static final HyperParametersPendulum HYPER_PAR =
            HyperParametersPendulum.FAIL_PEN_START_RANDOM;
    public static final int TIME_MAX_EVAL = 10;
    public static final double ANGLE_START_EVAL = 0.4;

    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        var dependencies = TrainerDependenciesFactory.createForTrainerRunning(HYPER_PAR);
        var trainer = TrainerPendulum.of(dependencies);
        var evaluator= PendulumAgentEvaluator.of(dependencies);
        trainer.train();
        timer.printInMs();
        plotting(trainer, dependencies, evaluator);
    }

    private static void plotting(TrainerPendulum trainer,
                                 TrainerDependencies dependencies,
                                 PendulumAgentEvaluator evaluator) {
        boolean isFail= evaluator.evaluate(TIME_MAX_EVAL,
                StartStateSupplierEnum.GIVEN_ANGLE_ZERO_SPEED.ofStartAngle(ANGLE_START_EVAL));
        System.out.println("isPark = " + isFail);
        plotTrainEvolution(trainer);
        var memoryPlotter= PendulumAgentMemoryPlotter.of(dependencies);
        memoryPlotter.plotAndSaveAll();
        plotTheta(evaluator);
        plotTorque(evaluator);
        plotSpd(evaluator);
    }

}
