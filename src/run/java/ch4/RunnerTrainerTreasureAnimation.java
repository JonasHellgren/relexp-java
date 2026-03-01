package ch4;


import chapters.ch4.domain.trainer.TrainerOneStepTdQLearning;
import chapters.ch4.implem.treasure.factory.TreasureRunnerFactory;
import chapters.ch4.implem_animation.AnimationTreasure;
import core.foundation.config.ConfigFactory;
import core.foundation.gadget.timer.CpuTimer;
import core.gridrl.TrainerGridDependencies;
import core.gridrl.TrainerGridI;

import static chapters.ch4.plotting.GridPlotShowAndSave.showAndSavePlots;

public class RunnerTrainerTreasureAnimation {
    public static final int NOF_DIGITS = 0;

    public static void main(String[] args) {
        var dep=TreasureRunnerFactory.produceDependencies().lowExploration();
        var trainer= TrainerOneStepTdQLearning.of(dep);
        var env=dep.environment();
        trainer.train(AnimationTreasure.create(env));
    }

    private static void plot(TrainerGridDependencies dep, TrainerGridI trainer) {
        var picPath = ConfigFactory.pathPicsConfig().ch4();
        var plotCfg= ConfigFactory.plotConfig();

        showAndSavePlots(
                dep,
                trainer.getRecorder(),
                "_treasureLowExpl",
                NOF_DIGITS, picPath,plotCfg);
    }

}
