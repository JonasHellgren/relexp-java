package chapters.ch6.domain.trainer.multisteps_after_episode;

import chapters.ch4.domain.helper.EpisodeGridInfo;
import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch4.domain.trainer.core.ExperienceGrid;
import chapters.ch6.domain.agent.core.AgentGridMultiStepI;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import core.foundation.util.list_array.MyListUtils;
import core.plotting.progress_plotting.ProgressMeasureTrajectories;
import core.plotting.progress_plotting.ProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Extracts progress measures from a list of experiences and multi-step results.
 * Needed for plotting
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressMeasureExtractorMultiStep {

    private final TrainerDependenciesMultiStep dependencies;

    public static ProgressMeasureExtractorMultiStep of(TrainerDependenciesMultiStep dependencies) {
        return new ProgressMeasureExtractorMultiStep(dependencies);
    }

    public ProgressMeasures getProgressMeasures(List<ExperienceGrid> experiences, MultiStepResultsGrid msResults) {
        var measureLists = getMeasureLists(experiences, msResults, dependencies.agent());
        var info = EpisodeGridInfo.of(experiences);
        return ProgressMeasures.builder()
                .sumRewards(info.sumRewards())
                .nSteps(info.nSteps())
                .tdError(MyListUtils.findAverage(measureLists.tdList()).orElseThrow())
                .tdBestAction(MyListUtils.findAverage(measureLists.tdBestActionList()).orElseThrow())
                .build();

    }

    private static ProgressMeasureTrajectories getMeasureLists(
            List<ExperienceGrid> experiences,
            MultiStepResultsGrid msResults,
            AgentGridMultiStepI agent) {
        var measureLists = ProgressMeasureTrajectories.empty();
        for (ExperienceGrid experience : experiences) {
            double valueSa = agent.read(StateActionGrid.of(experience.state(), experience.action()));
            double valueBestAction = agent.read(experience.state());
            var ms = msResults.resultAtStep(experiences.indexOf(experience));
            double valueTarget = agent.calculateValueTarget(ms);
            double td = Math.abs(valueTarget - valueSa);
            double tdBestAction = Math.abs(valueTarget - valueBestAction);
            measureLists.addTd(td);
            measureLists.addTdBestAction(Math.abs(tdBestAction));
        }
        return measureLists;
    }


}
