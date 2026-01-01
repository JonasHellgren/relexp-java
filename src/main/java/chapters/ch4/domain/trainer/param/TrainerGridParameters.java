package chapters.ch4.domain.trainer.param;

import lombok.Builder;
import lombok.With;
import org.apache.commons.math3.util.Pair;


/**
 * Represents the parameters for a trainer in the grid world domain.
 */
@With
@Builder
public record TrainerGridParameters(
        String environmentName,
        int nStepsMax, // Maximum number of steps per step
        int nEpisodes, // Total number of episodes to train for
        int nStepsHorizon, // Number of steps to look ahead when calculating returns
        Pair<Double, Double> learningRateStartAndEnd, // Learning rate range
        Pair<Double, Double> probRandomActionStartAndEnd, // Discount factor range
        double penaltyActionCorrection  //used when safety layer is present
) {

    public static TrainerGridParameters newDefault() {
        return new TrainerGridParameters("TBD",100,5000,5,Pair.create(0.9,0.1),Pair.create(0.1,0.01),1.0);
    }


}
