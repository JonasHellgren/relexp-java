package chapters.ch4.domain.trainer;

import chapters.ch4.domain.animation.AnimationGridI;
import com.google.common.base.Preconditions;
import core.gridrl.*;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
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
public class TrainerOneStepTdQLearning implements TrainerGridI {

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
    public void train(AnimationGridI animation) {
        var d = dependencies;
        recorder.clear();
        d.clearTimer();
        log.info("Starting training");
        animation.postEpisode(d.agent(), d.environment());
        animation.start();

        for (int ei = 0; ei < d.getNofEpisodes(); ei++) {
            var s = d.getStartState();
            d.resetBeforeEpisode();
            StepReturnGrid sr = null;
            while (d.notTerminalStateAndNotToManySteps(s)) {
                var action = d.chooseAction(s, ei);
                sr = d.takeAction(s, action);
                var e = ExperienceGrid.ofSars(s, action, sr);
                d.updateAgentMemoryFromExperience(e, ei);
                postStep(animation, s, ei, d, sr);
                s = sr.sNext();
                d.increaseStepCounter();
                d.saveExperienceForRecording(e);
            }
            postStep(animation, s, ei, d, sr);
            animation.postEpisode(d.agent(), d.environment());
            recorder.add(d.getProgressMeasures());
        }
        log.info("Training finished in (s): " + d.timer().timeInSecondsAsString());
    }

    private static void postStep(AnimationGridI animation, StateGrid s, int ei, TrainerGridDependencies d, StepReturnGrid sr) {
        animation.postStep(s, ei, d.getNofEpisodes(), d.probRandom(ei), sr.reward());
    }

}
