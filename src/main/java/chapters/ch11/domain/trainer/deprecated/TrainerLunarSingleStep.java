package chapters.ch11.domain.trainer.deprecated;

import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.core.TrainerI;
import chapters.ch11.factory.ProgressMeasuresFactory;
import chapters.ch11.helper.EpisodeCreator;
import core.plotting.progress_plotting.RecorderProgressMeasures;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;


/**
 * This class represents a single-step trainer for the 1 dimensional Lunar Lander game
 */

@AllArgsConstructor
@Getter
@Log
public class TrainerLunarSingleStep implements TrainerI {

    TrainerDependencies dependencies;
    RecorderProgressMeasures recorder;
    MemoryUpdaterSingleStepTrainer memoryUpdater;

    public static TrainerLunarSingleStep of(TrainerDependencies dependencies) {
        return new TrainerLunarSingleStep(
                dependencies,
                RecorderProgressMeasures.empty(),
                MemoryUpdaterSingleStepTrainer.of(dependencies));
    }

    @Override
    public void train() {
        var epCreator = EpisodeCreator.of(dependencies);
        var measuresFactory = ProgressMeasuresFactory.of(dependencies);
        recorder.clear();
        log.info("starting training");
        for (int i = 0; i < dependencies.getNofEpisodes(); i++) {
            var experiences = epCreator.create();
            memoryUpdater.fit(experiences);
            recorder.add(measuresFactory.getMeasures(experiences,null));
        }
    }
}
