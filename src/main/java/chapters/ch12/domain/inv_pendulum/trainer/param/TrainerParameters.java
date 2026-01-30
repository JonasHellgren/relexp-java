package chapters.ch12.domain.inv_pendulum.trainer.param;

import lombok.Builder;
import lombok.With;
import org.apache.commons.math3.util.Pair;

/**
 * Represents the parameters for a trainer in the inverted pendulum domain.
 * This includes the learning rate, discount factor, number of episodes,
 * probability of random actions, episodes between target updates,
 * mini-batch size, replay buffer size, and reward bounds.
 */
@Builder
@With
public record TrainerParameters(
        Pair<Double,Double> learningRateStartEnd,
        double discountFactor,
        int nEpisodes,
        Pair<Double,Double> probRandomActionStartEnd,
        int episodesBetweenTargetUpdates,
        int sizeMiniBatch,
        int sizeMiniBatchNominal,  //for miniBatchDependantLearningRate()
        int maxSizeReplayBuffer,
        Pair<Double,Double> minMaxReward
) {


}
