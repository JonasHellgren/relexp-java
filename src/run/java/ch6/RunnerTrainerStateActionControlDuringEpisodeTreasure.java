package ch6;

import chapters.ch6._shared.plotting.GridAgentPlotterMultiStep;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import chapters.ch6.domain.trainer.core.TrainerStateActionControlDuringEpisode;
import chapters.ch6.implem.factory.TrainerDependenciesFactory;
import core.foundation.config.ProjectPropertiesReader;
import core.foundation.gadget.timer.CpuTimer;
import core.plotting.progress_plotting.PlotterProgressMeasures;
import core.plotting.progress_plotting.ProgressMeasureEnum;
import core.plotting.progress_plotting.RecorderProgressMeasures;
import lombok.SneakyThrows;
import java.util.List;

public class RunnerTrainerStateActionControlDuringEpisodeTreasure {

    public static final int N_EPISODES = 50_000;
    public static final double LEARNING_RATE_START = 0.1;
    public static final int N_STEPS_HORIZON = 10;

    public static void main(String[] args) {
        var timer= CpuTimer.empty();
        var trainer =defineTrainer();
        trainer.train();
        timer.printInMs();
        var dependencies=trainer.getDependencies();
        showAndSavePlots(
                dependencies,
                trainer.getRecorder(),
                "_treasure_during_episodeNstep"+N_STEPS_HORIZON,
                0);
    }

    private static TrainerStateActionControlDuringEpisode defineTrainer() {
        var dependencies= TrainerDependenciesFactory.treasure(
                N_STEPS_HORIZON, N_EPISODES, LEARNING_RATE_START);
        return TrainerStateActionControlDuringEpisode.of(dependencies);
    }

    @SneakyThrows
    public static void showAndSavePlots(TrainerDependenciesMultiStep dependencies,
                                        RecorderProgressMeasures recorder,
                                        String fileNameAddOns, int nofDigits) {
        var agentPlotter= GridAgentPlotterMultiStep.of(dependencies, fileNameAddOns, nofDigits);
        agentPlotter.plotAndSaveStateValuesInFolderTempDiff();
        agentPlotter.plotAndSavePolicyInFolderTempDiff();
        var path= ProjectPropertiesReader.create().pathMultiStep();
        var progressPlotter = PlotterProgressMeasures.of(recorder, path, fileNameAddOns);
        progressPlotter.plotAndSave(List.of(
                ProgressMeasureEnum.RETURN,
                ProgressMeasureEnum.TD_ERROR,
                ProgressMeasureEnum.N_STEPS));
    }

}
