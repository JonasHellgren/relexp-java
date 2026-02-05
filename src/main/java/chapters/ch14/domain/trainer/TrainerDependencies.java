package chapters.ch14.domain.trainer;

import chapters.ch14.domain.environment.EnvironmentI;
import chapters.ch14.domain.environment.Experience;
import chapters.ch14.domain.environment.StepReturn;
import chapters.ch14.domain.interfaces.LongMemory;
import chapters.ch14.domain.planner.Planner;
import chapters.ch14.domain.planner.PlanningStatus;
import chapters.ch14.domain.settings.TrainerSettings;
import chapters.ch14.implem.pong.PongSettings;
import chapters.ch14.implem.pong_memory.BallHitFloorCalculator;
import com.google.common.base.Preconditions;
import core.foundation.gadget.cond.Counter;
import lombok.Builder;
import lombok.With;
import java.util.List;
import java.util.function.Supplier;

/**
 * This class represents the dependencies required for a trainer.
 * It includes the trainer settings, environment settings, environment, state supplier, planner,
 * long memory, replay buffer, mini batch adapter, and time to hit calculator.
 */
@Builder
@With
public record TrainerDependencies<SI, S, A>(
        TrainerSettings trainerSettings,
        PongSettings envSettings,
        EnvironmentI<S, A> environment,
        Supplier<S> stateSupplier,
        Planner<SI, S, A> planner,
        LongMemory<SI> longMemory,
        ReplayBuffer<SI, S, A> replayBuffer,
        MiniBatchAdapterI<SI, S, A> miniBatchAdapter,
        BallHitFloorCalculator timeToHitCalculator,
        Counter episCounter,
        Counter stepCounter
) {

    public void validate() {
        TrainerSettings settings = trainerSettings;
        Preconditions.checkArgument(settings.maxEpisodes() > 0,
                "maxEpisodes should be > 0");
        Preconditions.checkArgument(settings.maxSizeReplayBuffer()> settings.maxEpisodes()* settings.maxStepsPerEpisode(),
                "replayBuffer size should be > maxEpisodes");
    }

    public S getStartState() {
        return stateSupplier.get();
    }

    public PlanningStatus<A> plan(S state) {
        Supplier<S> ss = () -> state;
        return planner.plan(ss, longMemory);
    }


    public StepReturn<S> step(S s, PlanningStatus<A> planRes) {
        return environment.step(s, planRes.firstAction().orElseThrow());
    }

    public StepReturn<S> step(S s, A a) {
        return environment.step(s, a);
    }

    public List<Experience<S, A>> sampleMiniBatch() {
        return replayBuffer.sampleMiniBatch();
    }

    public void fitLongMemory(List<Experience<S, A>> mb) {
        var trainingData = miniBatchAdapter.adapt(mb);
        longMemory.fit(trainingData);
    }

    public void addExperience(S s, A a, StepReturn<S> sr) {
        replayBuffer.add(Experience.of(s, a, sr));
    }

    public boolean isShowLog() {
        return trainerSettings.isShowLogging();
    }

    public void maybeDeleteOldExperience() {
        replayBuffer.maybeDeleteOldExperience();
    }

    public void setMaxSteps(int maxStepsPerEpisode) {
        stepCounter.setMaxCount(maxStepsPerEpisode);
    }

    public void resetStepCounter() {
        stepCounter.reset();
    }

    public boolean isStepCounterNotExceeded() {
        return stepCounter.isNotExceeded();
    }

    public void increseStepCounter() {
        stepCounter.increase();
    }

    public void setMaxEpisodes(int nEpisodes) {
        episCounter.setMaxCount(nEpisodes);
    }

    public boolean isEpisCounterNotExceeded() {
        return episCounter.isNotExceeded();
    }

    public void increaseEpisCounter() {
        episCounter.increase();
    }
}
