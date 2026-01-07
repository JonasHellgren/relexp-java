package core.gridrl;

import core.foundation.util.collections.MyListUtils;
import core.plotting_rl.progress_plotting.ProgressMeasureTrajectories;
import core.plotting_rl.progress_plotting.ProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Factory class responsible for creating ProgressMeasures object from a list of ExperienceGrid
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressMeasuresExtractorGrid {

    private final TrainerGridDependencies dependencies;

    public static ProgressMeasuresExtractorGrid of(TrainerGridDependencies dependencies) {
        return new ProgressMeasuresExtractorGrid(dependencies);
    }

    public ProgressMeasures produce(List<ExperienceGrid> experiences) {
        var agent = dependencies.agent();
        var measureLists = getMeasureLists(experiences, agent);
        return create(experiences, measureLists);
    }

    private static ProgressMeasureTrajectories getMeasureLists(
            List<ExperienceGrid> experiences,
            AgentGridI agent) {
        var measureLists = ProgressMeasureTrajectories.empty();
        for (ExperienceGrid experience : experiences) {
            double valueSa = agent.read(experience.state(), experience.action());
            double valueBestAction = agent.readValue(experience.state());
            double valueTarget = agent.calculateValueTarget(experience);
            double td = Math.abs(valueTarget - valueSa);
            double tdBestAction = Math.abs(valueTarget - valueBestAction);
            measureLists.addTd(td);
            measureLists.addTdBestAction(Math.abs(tdBestAction));
        }
        return measureLists;
    }

    private ProgressMeasures create(List<ExperienceGrid> experiences,
                                    ProgressMeasureTrajectories measureLists) {
        var info = EpisodeGridInfo.of(experiences);
        return ProgressMeasures.builder()
                .sumRewards(info.sumRewards())
                .nSteps(info.nSteps())
                .tdError(MyListUtils.findAverage(measureLists.tdList()).orElseThrow())
                .tdBestAction(MyListUtils.findAverage(measureLists.tdBestActionList()).orElseThrow())
                .build();
    }

}
