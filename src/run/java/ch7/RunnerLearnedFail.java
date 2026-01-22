package ch7;

import chapters.ch7.domain.trainer.TrainerOneStepTdQLearningWithSafety;
import chapters.ch7.factory.TrainerDependencySafeFactory;
import chapters.ch7.plotter.ChartPlotterSafe;
import core.foundation.config.ConfigFactory;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import core.plotting_rl.progress_plotting.ProgressMeasures;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;

public class RunnerLearnedFail {

    public static final int N_EPISODES = 10_000;
    public static final double LEARNING_RATE_START = 0.1;
    public static final double PROB_RAND_START = 0.9;
    public static final int N_EPISODES_FEWER = 200;

    public static void main(String[] args) {
        var dependencies = TrainerDependencySafeFactory.treasure(
                N_EPISODES,
                LEARNING_RATE_START,
                PROB_RAND_START);
        var trainer = TrainerOneStepTdQLearningWithSafety.activeLearnerOf(dependencies);
        trainer.train();
        trainer.logTrainingTime();
        plotting(trainer);
    }

    private static void plotting(TrainerOneStepTdQLearningWithSafety trainer) {
        var recorder1 = trainer.getRecorder();
        ChartPlotterSafe.showAndSavePlots(trainer.getDependencies(),
                trainer.getRecorder(),
                "safe_learnedfails",
                ConfigFactory.plotConfig(),
                ConfigFactory.pathPicsConfig().ch7());

        var recorder2 = getRecorder2(recorder1, N_EPISODES_FEWER);
        ChartPlotterSafe.showAndSavePlots(trainer.getDependencies(),
                recorder2,
                "safe_learnedfails_fewerIter",
                ConfigFactory.plotConfig(),
                ConfigFactory.pathPicsConfig().ch7());
    }

    private static RecorderProgressMeasures getRecorder2(RecorderProgressMeasures recorder1, int i1) {
        var recorder2 = RecorderProgressMeasures.empty();
        var trajectory= recorder1.trajectory(ProgressMeasureEnum.SIZE_MEMORY);
        System.out.println("trajectory.size() = " + trajectory.size());
        for (int i = 0; i < i1; i++) {
            Integer sizeMem = Math.toIntExact(Math.round(trajectory.get(i)));
            var pm= ProgressMeasures.builder().sumRewards(0d).nSteps(0).nMemory(sizeMem).build();
            recorder2.add(pm);
        }
        return recorder2;
    }


}
