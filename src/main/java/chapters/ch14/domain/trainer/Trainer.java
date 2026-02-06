package chapters.ch14.domain.trainer;

import chapters.ch14.plotting.MeasuresCombLP;
import chapters.ch14.plotting.Recorder;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * This class represents a trainer for a reinforcement learning algorithm.
 * It provides a method to start the training process.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class Trainer<SI, S, A> {

    TrainerDependencies<SI, S, A> dependencies;
    @Getter
    Recorder recorder;

    public static <SI, S, A> Trainer<SI, S, A> of(TrainerDependencies<SI, S, A> dependencies) {
        return new Trainer<>(dependencies, Recorder.empty());
    }

    /**
     * Starts the training process.
     * <p>
     * The training process consists of multiple episodes, each with a maximum number of steps.
     * For each step, the trainer plans an action, takes a step, fits the model with a mini-batch of experiences,
     * adds the new experience to the replay buffer, and updates the state.
     * <p>
     * The training process continues until the maximum number of episodes is reached.
     */
    public void train() {
        var d = dependencies;
        d.validate();
        d.episCounter().reset();
        while (d.isEpisCounterNotExceeded()) {
            var measures = MeasuresCombLP.empty();
            var s = d.getStartState();
            boolean isTerminal = false;
            d.stepCounter().reset();
            while (d.isStepCounterNotExceeded() && !isTerminal) {
                var planRes = d.plan(s);
                var sr = d.step(s, planRes);
                fitMemoryFromReplayBuffer();
                d.maybeDeleteOldExperience();
                d.addExperience(s, planRes, sr);
                s = sr.stateNew();
                isTerminal = sr.isTerminal();
                d.increseStepCounter();
                measures.addReward(sr.reward());
            }
            recorder.add(measures);
            d.increaseEpisCounter();
        }
    }

    private void fitMemoryFromReplayBuffer() {
        var d = dependencies;
        for (int i = 0; i < d.trainerSettings().nFits(); i++) {
            var mb = d.sampleMiniBatch();
            ConditionalsUtil.executeIfTrue(!mb.isEmpty(), () -> d.fitLongMemory(mb));
        }
    }

    public String timeInSecondsAsString() {
        return dependencies.timer().timeInSecondsAsString();
    }

    public void resetTimer() {
        dependencies.timer().reset();
    }
}
