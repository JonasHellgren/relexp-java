package chapters.ch11.domain.trainer.param;

import lombok.With;


/**
 * Represents the parameters for a trainer in the Lunar Lander domain.
 * This includes the max steps, # episodes, gamma, and step horizon.
 */
@With
public record TrainerParameters(
        int nStepsMax, // Maximum number of steps per step
        int nEpisodes, // Total number of episodes to train for
        double gamma, // Discount factor for future rewards
        int nStepsHorizon // Number of steps to look ahead when calculating returns
) {

    public static TrainerParameters newDefault() {
        return new TrainerParameters(100,5000,0.99,5);
    }

    public double gammaPowN() {
        return Math.pow(gamma, nStepsHorizon);
    }

}
