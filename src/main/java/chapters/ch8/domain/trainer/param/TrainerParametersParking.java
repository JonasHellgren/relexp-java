package chapters.ch8.domain.trainer.param;

import lombok.Builder;
import lombok.With;
import org.apache.commons.math3.util.Pair;

/**
 * Represents the parameters for a trainer in the parking domain.
 * This includes the learning rates for action value and reward average,
 * the number of episodes, and the probability of random actions.
 */
@Builder
@With
public record TrainerParametersParking(
        Pair<Double,Double> learningRateActionValueStartEnd,
        Pair<Double,Double> learningRateRewardAverageStartEnd,
        Pair<Double,Double> probRandomActionStartEnd) {
}
