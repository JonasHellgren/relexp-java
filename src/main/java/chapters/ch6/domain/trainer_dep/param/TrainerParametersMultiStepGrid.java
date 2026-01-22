package chapters.ch6.domain.trainer_dep.param;

import lombok.Builder;
import lombok.With;
import org.apache.commons.math3.util.Pair;

@With
@Builder
public record TrainerParametersMultiStepGrid(
        int nStepsMax, // Maximum number of steps per step
        int nEpisodes, // Total number of episodes to train for
        double gamma, // Discount factor for future rewards
        Pair<Double, Double> learningRateStartAndEnd, // Learning rate range
        Pair<Double, Double> probRandomActionStartAndEnd, // Discount factor range
        int backupHorizon // Number of steps to look ahead when calculating returns
) {
}
