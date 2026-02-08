package ch4;


import chapters.ch4.domain.trainer.TrainerOneStepTdQLearning;
import chapters.ch4.implem.blocked_road_lane.factory.RoadRunnerFactory;
import core.foundation.config.ConfigFactory;
import core.gridrl.TrainerGridDependencies;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;

import static chapters.ch4.plotting.GridPlotShowAndSave.showAndSavePlots;


public class RunnerTrainerRoadAnimation {

    public static final int NOF_DIGITS = 2;
    public static final int N_EPISODES = 100;

    public static void main(String[] args) {
        var dep = RoadRunnerFactory.animation(N_EPISODES);
        var trainer = TrainerOneStepTdQLearning.of(dep);
        trainer.trainAnimation();
        plot(dep, trainer.getRecorder());
    }

    private static void plot(TrainerGridDependencies dep, RecorderProgressMeasures recorder) {
        var picPath = ConfigFactory.pathPicsConfig().ch4();
        var plotCfg = ConfigFactory.plotConfig();

        showAndSavePlots(dep,
                recorder,
                "_road_anim",
                NOF_DIGITS,
                picPath,
                plotCfg);


    }

}
