package ch7;

import chapters.ch7.domain.trainer.TrainerOneStepTdQLearningWithSafety;
import chapters.ch7.factory.TrainerDependencySafeFactory;
import core.foundation.gadget.timer.CpuTimer;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import core.plotting_rl.progress_plotting.ProgressMeasures;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;

import static chapters.ch7._shared.ChartPlotterSafe.showAndSavePlots;

public class RunnerLearnedFail {

    public static final int N_EPISODES = 10_000;
    public static final double LEARNING_RATE_START = 0.1;
    public static final double PROB_RAND_START = 0.9;
    public static final int N_EPISODES_FEWER = 200;

    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        var dependencies = TrainerDependencySafeFactory.treasure(
                N_EPISODES,
                LEARNING_RATE_START,
                PROB_RAND_START);
        var trainer = TrainerOneStepTdQLearningWithSafety.activeLearnerOf(dependencies);
        trainer.train();
        timer.printInMs();

        var recorder1 = trainer.getRecorder();
        showAndSavePlots(trainer.getDependencies(), recorder1, "safe_learnedfails", 1);

        var recorder2 = getRecorder2(recorder1, N_EPISODES_FEWER);
        showAndSavePlots(trainer.getDependencies(), recorder2, "safe_learnedfails_fewerIter", 1);

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
