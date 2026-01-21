package chapters.ch6.domain.trainers.after_episode;

import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import chapters.ch6.domain.trainer.core.TrainerI;
import core.gridrl.StateActionGrid;
import chapters.ch6.domain.trainer.episode_generator.EpisodeGeneratorGrid;
import chapters.ch6.domain.trainer.episode_generator.EpisodeInfo;
import chapters.ch6.domain.trainer.result_generator.MultiStepResultsGeneratorGrid;
import chapters.ch6.plotting.ProgressMeasureExtractorMultiStep;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * Trainer that implements state-action control after step is perfomed.

 //pseudo code:
 * while training termination criteria is false
 * experiences ← run step using present policy
 * for each step t in the step
 *   tn ← t+n
 *   s, r ← extract state and reward from experience t
 *   G(t) ← ∑_(k=t)^(min⁡(tn-1,T-1))▒〖γ^(t-k)∙r(k)〗
 *   sn ← the state n steps ahead from s
 *   if sn is present
 *    an ← the action taken n steps ahead from t
 *    G(t) ← G(t)+ γ^n∙Q(sn,an)
 *   Q(s,a)← Q(s,a)+α·(G-Q(s,a))
 *  endFor
 * endWhile
 *
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Log
public class TrainerStateActionControlAfterEpisode implements TrainerI {

    private final TrainerDependenciesMultiStep dependencies;
    private final RecorderProgressMeasures recorder;

    public static TrainerStateActionControlAfterEpisode of(TrainerDependenciesMultiStep dependencies) {
        return new TrainerStateActionControlAfterEpisode(dependencies,RecorderProgressMeasures.empty());
    }

    @Override
    public void train() {
        var d=dependencies;
        var generator = EpisodeGeneratorGrid.of(d);
        var msGenerator = MultiStepResultsGeneratorGrid.of(d);
        var measureExtractor = ProgressMeasureExtractorMultiStep.of(d);
        recorder.clear();
        for (int i = 0; i < d.getNofEpisodes(); i++) {
            double probRandom = d.calcProbRandomActiont(i);
            double learningRate = d.calcLearningRatet(i);
            var experiences = generator.generate(probRandom);
            var msResults = msGenerator.generate(experiences);
            var info= EpisodeInfo.of(experiences);
            recorder.add(measureExtractor.getProgressMeasures(experiences,msResults));
            for (int j = 0; j < msResults.size(); j++) {
                var mss = msResults.resultAtStep(j);
                if (info.isFirstVisit(StateActionGrid.of(mss.state(),mss.action()),j)) {
                    d.fitAgent(mss, learningRate);
                }
            }
        }
    }
}
