package chapters.ch5._shared.evaluation;

import core.foundation.util.math.LogarithmicDecay;
import lombok.Builder;
import lombok.With;
import org.apache.commons.math3.util.Pair;


/**
 * Settings for the policy evaluation algorithm.
 *
 * @param startAndEndLearningRate   learning rate, start and end
 * @param gamma       discount factor
 * @param nIterations number of iterations
 */
@Builder
@With
public record Settings(
        Pair<Double, Double> startAndEndLearningRate,
        double gamma,
        int nIterations,
        double probRandomAction
) {
    public static Settings of(Pair<Double, Double> learningRate, double gamma, int nIterations) {
        return Settings.ofRandomActionAlso(learningRate, gamma, nIterations,0.0);
    }

    public static Settings ofRandomActionAlso(Pair<Double, Double> learningRate,
                                              double gamma,
                                              int nIterations,
                                              double probRandomAction) {
        return Settings.builder()
                .startAndEndLearningRate(learningRate)
                .gamma(gamma)
                .nIterations(nIterations)
                .probRandomAction(probRandomAction)
                .build();
    }

    public LogarithmicDecay getDecayingLearningRate() {
        return LogarithmicDecay.of(startAndEndLearningRate.getFirst(), startAndEndLearningRate.getSecond(), nIterations());
    }


}
