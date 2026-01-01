package chapters.ch4.domain.trainer.core;

import chapters.ch4.domain.helper.TrainerOneStepTdHelper;
import com.google.common.base.Preconditions;
import core.plotting.progress_plotting.RecorderProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * This class implements the Q-Learning algorithm for one-step Temporal Difference (TD) trainers.
 * It uses the TrainerGridDependencies object to get the required dependencies for training.
 * The TrainerOneStepTdHelper class is used to handle helper functions like choosing actions, taking steps, etc.
 * The RecorderProgressMeasures object is used to record the progress of training.
 */
@Log
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TrainerOneStepTdQLearning implements TrainerGridI  {

    private final TrainerGridDependencies dependencies;
    private final TrainerOneStepTdHelper helper;
    private final RecorderProgressMeasures recorder;

    public static TrainerOneStepTdQLearning of(TrainerGridDependencies dependencies) {
        Preconditions.checkArgument(dependencies.isCorrectEnvironmentNamesForParameters(),
                "Non correct dependencies");
        return new TrainerOneStepTdQLearning(dependencies,
                TrainerOneStepTdHelper.of(dependencies),
                RecorderProgressMeasures.empty());
    }

    /**
     * Trains the agent using Q-Learning algorithm.
     */
    public void train() {
        var d = dependencies;
        var h = helper;
        recorder.clear();
        h.clearTimer();
        log.info("Starting training");
        for (int ei = 0; ei < d.getNofEpisodes(); ei++) {
            var s = d.getStartState();
            h.resetBeforeEpisode();
            while (h.notTerminalStateAndNotToManySteps(s)) {
                var action = h.chooseAction(s, ei);
                var sr = h.takeAction(s, action);
                var e = h.createSarsExperience(s, action, sr);
                h.updateAgentMemoryFromExperience(e,ei);
                s = sr.sNext();
                h.increaseStepCounter();
                h.saveExperienceForRecording(e);
            }
            recorder.add(h.getProgressMeasures());
        }
        log.info("Training finished in (s): " + h.getTimer().timeInSecondsAsString());
    }

}
