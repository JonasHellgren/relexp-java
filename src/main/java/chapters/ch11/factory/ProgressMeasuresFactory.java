package chapters.ch11.factory;

import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.trainer.core.ExperienceLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.multisteps.MultiStepResults;
import chapters.ch11.domain.trainer.core.EpisodeInfo;
import chapters.ch11.domain.trainer.multisteps.ValueCalculatorLunar;
import core.foundation.util.collections.ListUtil;
import core.plotting_rl.progress_plotting.ProgressMeasureTrajectories;
import core.plotting_rl.progress_plotting.ProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Factory class responsible for creating ProgressMeasures objects from a list of ExperienceLunar objects
 * and MultiStepResults.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressMeasuresFactory {

    private final TrainerDependencies dependencies;
    private final ValueCalculatorLunar calculator;

    public static ProgressMeasuresFactory of(TrainerDependencies dependencies) {
        return new ProgressMeasuresFactory(dependencies, ValueCalculatorLunar.of(dependencies));
    }

    public ProgressMeasures getMeasures(List<ExperienceLunar> experiences, MultiStepResults msrs) {
        var agent = dependencies.agent();
        var measures = ProgressMeasureTrajectories.empty();
        for (ExperienceLunar experience : experiences) {
            addTempDiffErrors(experience, measures);
            addStandardDeviation(experience, agent, measures);
            maybeAddGradients(experiences, msrs, experience, measures);
        }
        return create(experiences, measures);
    }

    private void addTempDiffErrors(ExperienceLunar experience,
                                   ProgressMeasureTrajectories measureLists) {
        var ap = dependencies.agent().getAgentParameters();
        double td0 = calculator.calcTemporalDifferenceError(experience);
        double td = ap.clipTdError(td0);
        double tdErrorBestAction = calculator.calcTemporalDifferenceErrorBestAction(experience);
        measureLists.addTd(Math.abs(td0));
        measureLists.addTdClipped(Math.abs(td));
        measureLists.addTdBestAction(Math.abs(tdErrorBestAction));
    }

    private static void addStandardDeviation(ExperienceLunar experience,
                                             AgentLunar agent,
                                             ProgressMeasureTrajectories measureLists) {
        var mAndStd = agent.readActor(experience.state());
        measureLists.addStd(mAndStd.std());
    }

    private void maybeAddGradients(List<ExperienceLunar> experiences,
                                   MultiStepResults msrs,
                                   ExperienceLunar experience,
                                   ProgressMeasureTrajectories measureLists) {
        if (msrs == null) {
            measureLists.addGradMean(0);
            measureLists.addGradMeanClipped(0);
        } else {
            var ap = dependencies.agent().getAgentParameters();
            double adv = msrs.advantageAtStep(experiences.indexOf(experience));
            var grad = dependencies.agent().gradientMeanAndLogStd(experience.state(), experience.action());
            var gradClipped = grad.clip(ap.gradMeanMax(), ap.gradStdMax());
            measureLists.addGradMean(gradClipped.mean() * adv);
            measureLists.addGradMeanClipped(gradClipped.mean() * adv);
        }
    }

    private ProgressMeasures create(List<ExperienceLunar> experiences,
                                    ProgressMeasureTrajectories measures) {
        var info = EpisodeInfo.of(experiences);
        return ProgressMeasures.builder()
                .sumRewards(info.sumRewards())
                .nSteps(info.nSteps())
                .tdError(ListUtil.findAverage(measures.tdList()).orElseThrow())
                .tdErrorClipped(ListUtil.findAverage(measures.tdListClipped()).orElseThrow())
                .gradMean(ListUtil.findAverage(measures.gradMeanList()).orElseThrow())
                .gradMeanClipped(ListUtil.findAverage(measures.gradMeanListClipped()).orElseThrow())
                .stdActor(ListUtil.findAverage(measures.stdList()).orElseThrow())
                .tdBestAction(ListUtil.findAverage(measures.tdBestActionList()).orElseThrow())
                .build();

    }

}
