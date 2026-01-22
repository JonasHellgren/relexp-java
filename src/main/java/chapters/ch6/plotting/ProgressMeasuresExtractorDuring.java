package chapters.ch6.plotting;

import chapters.ch6.domain.trainer_dep.core.ReturnCalculator;
import core.gridrl.EpisodeGridInfo;
import core.gridrl.ExperienceGrid;
import chapters.ch6.domain.trainer_dep.core.TrainerDependenciesMultiStep;
import core.foundation.util.collections.ListUtil;
import core.plotting_rl.progress_plotting.ProgressMeasures;
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
    ReturnCalculator returnCalculator;

    public static ProgressMeasuresExtractorDuring of(TrainerDependenciesMultiStep dependencies) {
        return new ProgressMeasuresExtractorDuring(
                dependencies,
                ReturnCalculator.of(dependencies));
    }

    public ProgressMeasures getProgressMeasures(List<ExperienceGrid> experiences) {
        var info = EpisodeGridInfo.of(experiences);
        var errors = getTdErrors(experiences);
        return ProgressMeasures.builder()
                .sumRewards(info.sumRewards())
                .nSteps(info.nSteps())
                .tdError(ListUtil.findAverage(errors).orElseThrow())
                .tdBestAction(0d)
                .build();
    }

    private List<Double> getTdErrors(
            List<ExperienceGrid> experiences) {
        List<Double> tdErrors = new ArrayList<>();
        for (int t = 0; t < experiences.size(); t++) {
            var exp = experiences.get(t);
            double valueSa = dependencies.agent().read(exp.state(), exp.action());
            double returnAtTau = returnCalculator.calculateReturnAtTau(t, experiences);
            tdErrors.add(Math.abs(returnAtTau - valueSa));
        }
        return tdErrors;
    }


}
