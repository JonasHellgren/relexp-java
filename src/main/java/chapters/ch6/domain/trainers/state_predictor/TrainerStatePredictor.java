package chapters.ch6.domain.trainers.state_predictor;

import chapters.ch6.domain.trainer.episode_generator.EpisodeGeneratorGrid;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import chapters.ch6.domain.trainer.core.TrainerI;
import chapters.ch6.domain.trainer.result_generator.MultiStepResultsGeneratorGrid;
import core.foundation.util.math.LogarithmicDecay;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A trainer that predicts the state of an agent using a multi-step approach.
 * <p>
 * /pseudo code:
 * while training termination criteria is false
 * experiences ← run step using present policy
 * for each step t in the step
 * tn ← t+n
 * s, r ← extract state and reward from experience t
 * G(t) ← ∑_(k=t)^(min⁡(tn-1,T-1))▒〖γ^(t-k)∙r(k)〗
 * sn ← the state n steps ahead from s
 * if sn is present
 * G(t) ← G(t)+ γ^n∙V(sn)
 * V(s)← V(s)+α·(G-V(s))
 * endFor
 * endWhile
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TrainerStatePredictor implements TrainerI {

    private static final double PROB_RANDOM = 0.1;
    private final TrainerDependenciesMultiStep dependencies;

    public static TrainerStatePredictor of(TrainerDependenciesMultiStep dependencies) {
        return new TrainerStatePredictor(dependencies);
    }

    @Override
    public void train() {
        var generator = EpisodeGeneratorGrid.of(dependencies);
        var lr = dependencies.trainerParameters().learningRateStartAndEnd();
        var decLearningRate = LogarithmicDecay.of(lr.getFirst(), lr.getSecond(), dependencies.getNofEpisodes());
        var msGenerator = MultiStepResultsGeneratorGrid.of(dependencies);

        for (int i = 0; i < dependencies.getNofEpisodes(); i++) {
            var experienceList = generator.generate(PROB_RANDOM);
            var msResults = msGenerator.generate(experienceList);
            var agent = dependencies.agent();
            double learningRate = decLearningRate.calcOut(i);
            for (int j = 0; j < msResults.size(); j++) {
                agent.fit(msResults.resultAtStep(j), learningRate);
            }
        }
    }
}
