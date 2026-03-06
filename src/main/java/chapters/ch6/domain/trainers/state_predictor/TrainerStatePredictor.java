package chapters.ch6.domain.trainers.state_predictor;

import chapters.ch6.domain.animation.AnimationGridMsEmpty;
import chapters.ch6.domain.animation.AnimationGridMultiStepI;
import chapters.ch6.domain.trainer_dep.episode_generator.EpisodeGeneratorGrid;
import chapters.ch6.domain.trainer_dep.core.TrainerDependenciesMultiStep;
import chapters.ch6.domain.trainer_dep.core.TrainerI;
import chapters.ch6.domain.trainer_dep.result_generator.MultiStepResultGrid;
import chapters.ch6.domain.trainer_dep.result_generator.MultiStepResultsGeneratorGrid;
import core.foundation.gadget.math.LogarithmicDecay;
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
        train(AnimationGridMsEmpty.create());
    }

    public void train(AnimationGridMultiStepI animation) {
        var d=dependencies;
        var generator = EpisodeGeneratorGrid.of(d);
        var lr = d.trainerParameters().learningRateStartAndEnd();
        var decLearningRate = LogarithmicDecay.of(lr.getFirst(), lr.getSecond(), d.getNofEpisodes());
        var msGenerator = MultiStepResultsGeneratorGrid.of(d);
        animation.start();

        for (int ei = 0; ei < d.getNofEpisodes(); ei++) {
            var experienceList = generator.generate(PROB_RANDOM);
            var msResults = msGenerator.generate(experienceList);
            var agent = d.agent();
            double learningRate = decLearningRate.calcOut(ei);
            for (int si = 0; si < msResults.size(); si++) {
                var ras = msResults.resultAtStep(si);
                agent.fit(ras, learningRate);
                postToAnimation(animation, ras, ei, d);
            }

        }
    }

    private static void postToAnimation(AnimationGridMultiStepI animation,
                                        MultiStepResultGrid ras,
                                        int ei,
                                        TrainerDependenciesMultiStep d) {
        animation.postStep(ras.state(), ei,(int) d.getNofEpisodes(), 0, ras.sumRewards());
        animation.postEpisode(d.agent(), d.environment(), ei);
    }


}
