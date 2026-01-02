package chapters.ch6.domain.trainer.multisteps_after_episode;

import chapters.ch4.domain.trainer.core.ExperienceGrid;
import chapters.ch6._shared.info.EpisodeInfo;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import com.google.common.base.Preconditions;
import core.learningutils.MyRewardListUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Generates multi-step results from a list of experiences.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiStepResultsGeneratorGrid {

    private final TrainerDependenciesMultiStep dependencies;

    public static MultiStepResultsGeneratorGrid of(TrainerDependenciesMultiStep dependencies) {
        return new MultiStepResultsGeneratorGrid(dependencies);
    }

    public MultiStepResultsGrid generate(List<ExperienceGrid> experiences) {
        var informer = EpisodeInfo.of(experiences);
        var results = MultiStepResultsGrid.create(informer.nSteps());
        IntStream.range(0, informer.nSteps())
                .mapToObj(time -> createResultAtTime(time, informer))
                .forEach(results::add);
        return results;
    }


    private MultiStepResultGrid createResultAtTime(int time, EpisodeInfo informer) {
        int nExperiences = informer.nSteps();
        var parameters = dependencies.trainerParameters();
        int idxEnd = time + parameters.backupHorizon();
        validate(time, nExperiences, idxEnd);
        var rewards = informer.getRewards(time, idxEnd);
        double rewardSum = MyRewardListUtils.discountedSum(rewards, parameters.gamma());
        var stateFut = informer.getStateFutureOptional(idxEnd);
        var actionFuture= informer.getActionFutureOptional(idxEnd);
        var e = informer.experienceAtTime(time);
        return MultiStepResultGrid.builder()
                .state(e.state())
                .action(e.action())
                .sumRewards(rewardSum)
                .stateFuture(stateFut)
                .actionFuture(actionFuture)
                .build();
    }


    private static void validate(int time, int nExperiences, int idxEnd) {
        Preconditions.checkArgument(time < nExperiences, "Non valid start index, time=" + time);
        Preconditions.checkArgument(idxEnd > time, "Non valid end index, idxEnd=" + idxEnd + ", time=" + time);
    }

}
