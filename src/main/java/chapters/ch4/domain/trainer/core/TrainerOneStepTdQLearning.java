package chapters.ch4.domain.trainer.core;

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
    private final RecorderProgressMeasures recorder;

    public static TrainerOneStepTdQLearning of(TrainerGridDependencies dependencies) {
        Preconditions.checkArgument(dependencies.isCorrectEnvironmentNamesForParameters(),
                "Non correct dependencies");
        return new TrainerOneStepTdQLearning(dependencies,
                RecorderProgressMeasures.empty());
    }

    /**
     * Trains the agent using Q-Learning algorithm.
     */
    public void train() {
        var d = dependencies;
        recorder.clear();
        d.clearTimer();
        log.info("Starting training");
        for (int ei = 0; ei < d.getNofEpisodes(); ei++) {
            var s = d.getStartState();
            d.resetBeforeEpisode();
            while (d.notTerminalStateAndNotToManySteps(s)) {
                var action = d.chooseAction(s, ei);
                var sr = d.takeAction(s, action);
                var e=ExperienceGrid.ofSars(s, action, sr);
                d.updateAgentMemoryFromExperience(e,ei);
                s = sr.sNext();
                d.increaseStepCounter();
                d.saveExperienceForRecording(e);
            }
            recorder.add(d.getProgressMeasures());
        }
        log.info("Training finished in (s): " + d.timer().timeInSecondsAsString());
    }

}
