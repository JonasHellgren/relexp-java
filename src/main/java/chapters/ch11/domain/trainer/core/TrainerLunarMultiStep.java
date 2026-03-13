package chapters.ch11.domain.trainer.core;

import chapters.ch11.animation.AnimationLunar;
import chapters.ch11.domain.trainer.multisteps.MultiStepResults;
import chapters.ch11.domain.trainer.multisteps.MultiStepResultsGenerator;
import chapters.ch11.domain.trainer.multisteps.TrainingDataCreator;
import chapters.ch11.factory.ProgressMeasuresFactory;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.commons.math3.util.Pair;


/**
 * This class represents a multi-step trainer for the 1 dimensional Lunar Lander game
 */

@AllArgsConstructor
@Getter
@Log
public class TrainerLunarMultiStep {

    private final TrainerDependencies dependencies;
    private final RecorderProgressMeasures recorder;

    public static TrainerLunarMultiStep of(TrainerDependencies dependencies) {
        return new TrainerLunarMultiStep(dependencies,
                RecorderProgressMeasures.empty());
    }

    public void train() {
        train(AnimationLunar.empty());
    }


    public void train(AnimationLunar animation) {
        var epCreator = EpisodeCreator.of(dependencies);
        var measuresFactory = ProgressMeasuresFactory.of(dependencies);
        var msrGenerator = MultiStepResultsGenerator.of(dependencies);
        recorder.clear();
        animation.start();
        log.info("starting training");
        for (int i = 0; i < dependencies.getNofEpisodes(); i++) {
            var experiences = epCreator.create();
            var msr = msrGenerator.generate(experiences);
            fit(msr);
            animateEpis(msr, Pair.create(i, dependencies.getNofEpisodes()),animation);
            animation.postEpisode(dependencies.agent(),i);
            recorder.add(measuresFactory.getMeasures(experiences,msr));
        }
    }

    private void fit(MultiStepResults msr) {
        var dataCreator = TrainingDataCreator.of(dependencies);
        var agent = dependencies.agent();
        for (int step = 0; step < msr.nResults(); step++) {
            var data=dataCreator.create(msr, step);
            agent.fitCritic(data.dataCritic());
            agent.fitActor(data.dataMean(), data.dataStd());
        }
    }


    private void animateEpis(MultiStepResults msr, Pair<Integer,Integer> epis, AnimationLunar animation) {
        for (int step = 0; step < msr.nResults(); step++) {
            animation.postStep(epis, msr.experienceAtStep(step));
        }
    }

    public void logTimer() {
        log.info("timer: " + dependencies.timeInSecondsAsString());
    }
}
