package chapters.ch6.domain.trainer.mutlisteps_during_epis;

import chapters.ch4.domain.helper.EpisodeGridInfo;
import chapters.ch4.domain.trainer.core.ExperienceGrid;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import core.foundation.util.collections.MyListUtils;
import core.plotting.progress_plotting.ProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts progress measures from a list of experiences and multi-step results.
 * Needed for plotting
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressMeasuresExtractorDuring {

    TrainerDependenciesMultiStep dependencies;
    MultiStepMemoryUpdater memoryUpdater;

    public static ProgressMeasuresExtractorDuring of(TrainerDependenciesMultiStep dependencies, MultiStepMemoryUpdater updater) {
        return new ProgressMeasuresExtractorDuring(dependencies,updater);
    }

    public ProgressMeasures getProgressMeasures(List<ExperienceGrid> experiences) {
        var info = EpisodeGridInfo.of(experiences);
        var errors = getTdErrors(experiences);
        return ProgressMeasures.builder()
                .sumRewards(info.sumRewards())
                .nSteps(info.nSteps())
                .tdError(MyListUtils.findAverage(errors).orElseThrow())
                .tdBestAction(0d)
                .build();    }


    private List<Double> getTdErrors(
            List<ExperienceGrid> experiences) {
        List<Double> tdErrors = new ArrayList<>();
        for (int t = 0; t < experiences.size(); t++) {
            var exp = experiences.get(t);
            double valueSa = dependencies.agent().read(exp.state(), exp.action());
            double returnAtTau = memoryUpdater.calculateReturnAtTau(t, experiences);
            tdErrors.add(Math.abs(returnAtTau - valueSa));
        }
        return tdErrors;
    }


}
