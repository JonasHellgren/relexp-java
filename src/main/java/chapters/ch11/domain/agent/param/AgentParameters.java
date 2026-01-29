package chapters.ch11.domain.agent.param;

import chapters.ch11.domain.environment.param.LunarParameters;
import core.foundation.util.math.MathUtil;
import lombok.Builder;
import lombok.With;

/**
 * This class represents the parameters for an agent in the Lunar Lander game.
 * It includes parameters for the actor and critic, as well as hyperparameters for training.

 TODO: move learning rates to trainer
 */
@With
@Builder
public record AgentParameters(
        int nKernelsY, // Number of radial basis functions for the actor in the y direction
        int nKernelsSpeed, // Number of radial basis functions for the actor in the speed direction
        double[] gammas, // Gammas for the critic
        double initWeightLogStd, // Initial weight for the log standard deviation of the actor
        double learningRateCritic, // Learning rate for the critic
        double learningRateActor, // Learning rate for the actor
        int nEpochs, // Number of epochs for training
        double gradMeanMax, // Maximum gradient for the mean of the actor
        double gradStdMax, // Maximum gradient for the standard deviation of the actor
        double tdMax, // Maximum TD error for training
        double advMax // Maximum advantage for training
) {



    public double clipAdvantage(double adv0) {
        return MathUtil.clip(adv0, -advMax, advMax);
    }

    public double clipTdError(double e0) {
        return MathUtil.clip(e0, -tdMax(), tdMax());
    }


}
