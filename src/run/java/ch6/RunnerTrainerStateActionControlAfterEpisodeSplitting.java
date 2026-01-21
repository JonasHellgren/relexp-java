package ch6;

import chapters.ch6.plotting.GridAgentPlotterMultiStep;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import chapters.ch6.domain.trainers.after_episode.TrainerStateActionControlAfterEpisode;
import chapters.ch6.implem.factory.TrainerDependenciesFactorySplitting;
import core.foundation.config.ConfigFactory;
import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting_rl.progress_plotting.PlotterProgressMeasures;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
import lombok.SneakyThrows;
import java.util.List;

public class RunnerTrainerStateActionControlAfterEpisodeSplitting {

    public static final int N_EPISODES = 1000;
    public static final double LEARNING_RATE_START = 0.1;
    public static final int N_STEPS_HORIZON = 3;

    public static void main(String[] args) {
        var trainer =defineTrainer();
        trainer.train();
        var dependencies=trainer.getDependencies();
        var memory=dependencies.agent().getMemory();
        System.out.println("memory = " + memory);
        showAndSavePlots(dependencies, trainer.getRecorder(), "_splitting_after_episode", 2);
    }

    private static TrainerStateActionControlAfterEpisode defineTrainer() {
        var dependencies= TrainerDependenciesFactorySplitting.learnPolicySplittingAfterEpis(
                N_STEPS_HORIZON, N_EPISODES, LEARNING_RATE_START);
        return TrainerStateActionControlAfterEpisode.of(dependencies);
    }

    @SneakyThrows
    public static void showAndSavePlots(TrainerDependenciesMultiStep dependencies,
                                        RecorderProgressMeasures recorder,
                                        String fileNameAddOns, int nofDigits) {
        var plotCfg= ConfigFactory.plotConfig();
        var agentPlotter= GridAgentPlotterMultiStep.of(dependencies, fileNameAddOns, nofDigits,plotCfg);
        agentPlotter.plotAndSaveStateValuesInFolderTempDiff();
        agentPlotter.plotAndSavePolicyInFolderTempDiff();
        var path= ProjectPropertiesReader.create().pathMultiStep();
        System.out.println("path = " + path);
        var progressPlotter = PlotterProgressMeasures.of(recorder, path, fileNameAddOns);
        progressPlotter.plotAndSave(List.of(
                ProgressMeasureEnum.RETURN,
                ProgressMeasureEnum.TD_ERROR,
                ProgressMeasureEnum.N_STEPS));


    }

}
