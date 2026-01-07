package chapters.ch11.domain.trainer.core;

import chapters.ch11.domain.trainer.multisteps.MultiStepResults;
import chapters.ch11.domain.trainer.multisteps.MultiStepResultsGenerator;
import chapters.ch11.domain.trainer.multisteps.TrainingDataCreator;
import chapters.ch11.factory.ProgressMeasuresFactory;
import chapters.ch11.helper.EpisodeCreator;
import core.foundation.gadget.timer.CpuTimer;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * This class represents a multi-step trainer for the 1 dimensional Lunar Lander game
 */

@AllArgsConstructor
@Getter
@Log
public class TrainerLunarMultiStep implements TrainerI {

    private final TrainerDependencies dependencies;
    private final RecorderProgressMeasures recorder;

    public static TrainerLunarMultiStep of(TrainerDependencies dependencies) {
        return new TrainerLunarMultiStep(dependencies,
                RecorderProgressMeasures.empty());
    }

    @Override
    public void train() {
        var epCreator = EpisodeCreator.of(dependencies);
        var measuresFactory = ProgressMeasuresFactory.of(dependencies);
        var msrGenerator = MultiStepResultsGenerator.of(dependencies);
        var timer= CpuTimer.empty();
        recorder.clear();
        log.info("starting training");
        for (int i = 0; i < dependencies.getNofEpisodes(); i++) {
            var experiences = epCreator.create();
            var msr = msrGenerator.generate(experiences);
            fit(msr);
            recorder.add(measuresFactory.getMeasures(experiences,msr));
        }
        log.info("Training finished in (s): " + timer.timeInSecondsAsString());
    }

    private void fit(MultiStepResults msr) {
        var dataCreator = TrainingDataCreator.of(dependencies);
        var agent = dependencies.agent();
        for (int step = 0; step < msr.nResults(); step++) {
            var data=dataCreator.create(msr, step);
            agent.fitCritic(data.dataCritic());
            agent.fitActorUseCriticActivations(data.dataMean(), data.dataStd());
        }
    }

}
