package ch12;


import chapters.ch12.domain.inv_pendulum.agent.evaluator.PendulumAgentEvaluator;
import chapters.ch12.domain.inv_pendulum.environment.startstate_supplier.StartStateSupplierEnum;
import chapters.ch12.domain.inv_pendulum.trainer.core.TrainerDependencies;
import chapters.ch12.domain.inv_pendulum.trainer.core.TrainerPendulum;
import chapters.ch12.factory.HyperParametersPendulum;
import chapters.ch12.factory.TrainerDependenciesFactory;
import chapters.ch12.plotting_invpend.PendulumAgentMemoryPlotter;
import core.foundation.config.ConfigFactory;

import static chapters.ch12.plotting_invpend.TrainerPlotter.*;

public class RunnerPendulumTrainer {
    static final HyperParametersPendulum HYPER_PAR =
            HyperParametersPendulum.FAIL_PEN_START_RANDOM;
    public static final int TIME_MAX_EVAL = 10;
    public static final double ANGLE_START_EVAL = 0.4;

    public static void main(String[] args) {
        var dependencies = TrainerDependenciesFactory.createForTrainerRunning(HYPER_PAR);
        var trainer = TrainerPendulum.of(dependencies);
        var evaluator= PendulumAgentEvaluator.of(dependencies);
        trainer.train();
        trainer.logTime();
        plotting(trainer, dependencies, evaluator);
    }

    private static void plotting(TrainerPendulum trainer,
                                 TrainerDependencies dependencies,
                                 PendulumAgentEvaluator evaluator) {
        boolean isFail= evaluator.evaluate(TIME_MAX_EVAL,
                StartStateSupplierEnum.GIVEN_ANGLE_ZERO_SPEED.ofStartAngle(ANGLE_START_EVAL));
        if (isFail) {
            System.out.println("Pendulum failed!");
            return;
        }
        var path= ConfigFactory.pathPicsConfig().ch12();
        plotTrainEvolution(trainer,path);
        var memoryPlotter= PendulumAgentMemoryPlotter.of(dependencies);
        memoryPlotter.plotAndSaveAll(path);
        plotTheta(evaluator);
        plotTorque(evaluator);
        plotSpd(evaluator);
    }

}
