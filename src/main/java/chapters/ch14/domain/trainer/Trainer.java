package chapters.ch14.domain.trainer;

import chapters.ch14.plotting.MeasuresCombLP;
import chapters.ch14.plotting.Recorder;
import core.foundation.gadget.cond.Counter;
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
        return new Trainer<>(dependencies,Recorder.empty());
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
        dependencies.validate();
        var settings = dependencies.trainerSettings();
        var episCounter = Counter.ofMaxCount(settings.maxEpisodes());
        while (episCounter.isNotExceeded()) {
            var stepCounter = Counter.ofMaxCount(settings.maxStepsPerEpisode());
            var measures= MeasuresCombLP.empty();
            var s = dependencies.getStartState();
            boolean isTerminal = false;
            while (stepCounter.isNotExceeded() && !isTerminal) {
                var planRes = dependencies.plan(s);
                A a = planRes.firstAction().orElseThrow();
                var sr = dependencies.step(s, a);
                for (int i = 0; i < dependencies.trainerSettings().nFits(); i++) {
                    var mb = dependencies.sampleMiniBatch();
                    ConditionalsUtil.executeIfTrue(!mb.isEmpty(), () -> dependencies.fitLongMemory(mb));
                }
                dependencies.maybeDeleteOldExperience();
                dependencies.addExperience(s, a, sr);
                s = sr.stateNew();
                isTerminal = sr.isTerminal();
                stepCounter.increase();
                measures.addReward(sr.reward());
            }
            recorder.add(measures);
            episCounter.increase();
        }
    }

}
