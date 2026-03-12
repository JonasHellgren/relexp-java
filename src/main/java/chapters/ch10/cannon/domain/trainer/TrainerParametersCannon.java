package chapters.ch10.cannon.domain.trainer;

import lombok.Builder;
import lombok.With;

/**
 * Represents the parameters for a trainer in the cannon domain.
 * This includes the minimum gradient log denominator, number of episodes,
 * learning rate start, and learning rate end.
 */
@Builder
@With
public record TrainerParametersCannon(
        double denomMinGradLog,
        int nEpisodes,
        double learningRateStart,
        double learningRateEnd
) {
}
