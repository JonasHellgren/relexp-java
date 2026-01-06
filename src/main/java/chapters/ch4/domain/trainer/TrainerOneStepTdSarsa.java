package chapters.ch4.domain.trainer;

import core.gridrl.ExperienceGrid;
import core.gridrl.TrainerGridDependencies;
import core.gridrl.TrainerGridI;
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
    private final RecorderProgressMeasures recorder;

    public static TrainerOneStepTdSarsa of(TrainerGridDependencies dependencies) {
        return new TrainerOneStepTdSarsa(dependencies,
                RecorderProgressMeasures.empty());
    }

    /**
     * Trains the agent using the Sarsa algorithm.
     * This method iterates over the specified number of episodes, choosing actions,
     * taking steps, and updating agent memory at each step.
     */
    public void train() {
        var d = dependencies;
        recorder.clear();
        d.clearTimer();
        recorder.clear();
        log.info("starting training");
        for (int ei = 0; ei < d.getNofEpisodes(); ei++) {
            var s = d.getStartState();
            var a = d.chooseAction(s, ei);
            d.resetBeforeEpisode();
            while (d.notTerminalStateAndNotToManySteps(s)) {
                var sr = d.takeAction(s, a);
                var aNext = d.chooseAction(sr.sNext(), ei);
                var e = ExperienceGrid.ofSarsa(s, a, sr,aNext);
                d.updateAgentMemoryFromExperience(e,ei);
                s = sr.sNext();
                a = aNext;
                d.increaseStepCounter();
                d.saveExperienceForRecording(e);
            }
            recorder.add(d.getProgressMeasures());
        }
        log.info("Training finished in (s): " + d.timer().timeInSecondsAsString());
    }

}
