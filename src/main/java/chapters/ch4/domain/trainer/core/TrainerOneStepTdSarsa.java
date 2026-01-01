package chapters.ch4.domain.trainer.core;

import chapters.ch4.domain.helper.TrainerOneStepTdHelper;
import core.plotting.progress_plotting.RecorderProgressMeasures;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * Implementation of the Sarsa algorithm for one-step Temporal Difference (TD) trainers.
 * This class provides methods for training an agent using the Sarsa algorithm.
 */
@AllArgsConstructor
@Log
@Getter
public class TrainerOneStepTdSarsa  implements TrainerGridI {
    private final TrainerGridDependencies dependencies;
    private final TrainerOneStepTdHelper helper;
    private final RecorderProgressMeasures recorder;

    public static TrainerOneStepTdSarsa of(TrainerGridDependencies dependencies) {
        return new TrainerOneStepTdSarsa(dependencies,
                TrainerOneStepTdHelper.of(dependencies),
                RecorderProgressMeasures.empty());
    }

    /**
     * Trains the agent using the Sarsa algorithm.
     * This method iterates over the specified number of episodes, choosing actions,
     * taking steps, and updating agent memory at each step.
     */
    public void train() {
        var d = dependencies;
        var h = helper;
        recorder.clear();
        h.clearTimer();
        recorder.clear();
        log.info("starting training");
        for (int ei = 0; ei < d.getNofEpisodes(); ei++) {
            var s = d.getStartState();
            var a = h.chooseAction(s, ei);
            h.resetBeforeEpisode();
            while (h.notTerminalStateAndNotToManySteps(s)) {
                var sr = h.takeAction(s, a);
                var aNext = h.chooseAction(sr.sNext(), ei);
                var e = h.createSarsaExperience(s, a, sr, aNext);
                h.updateAgentMemoryFromExperience(e,ei);
                s = sr.sNext();
                a = aNext;
                h.increaseStepCounter();
                h.saveExperienceForRecording(e);
            }
            recorder.add(h.getProgressMeasures());
        }
        log.info("Training finished in (s): " + h.getTimer().timeInSecondsAsString());
    }

}
