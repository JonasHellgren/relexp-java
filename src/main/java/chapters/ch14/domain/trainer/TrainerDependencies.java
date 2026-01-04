package chapters.ch14.domain.trainer;

import chapters.ch14.domain.environment.EnvironmentI;
import chapters.ch14.domain.environment.Experience;
import chapters.ch14.domain.environment.StepReturn;
import chapters.ch14.domain.long_memory.LongMemory;
import chapters.ch14.domain.planner.Planner;
import chapters.ch14.domain.planner.PlanningStatus;
import chapters.ch14.domain.settings.TrainerSettings;
import chapters.ch14.environments.pong.PongSettings;
import chapters.ch14.environments.pong_memory.BallHitFloorCalculator;
import com.google.common.base.Preconditions;
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
        BallHitFloorCalculator timeToHitCalculator
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

}
