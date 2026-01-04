package chapters.ch11.domain.agent.param;

import chapters.ch11.domain.environment.param.LunarParameters;
import core.foundation.util.math.MyMathUtils;
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
        int batchSize, // Batch size for training
        int nEpochs, // Number of epochs for training
        double gradMeanMax, // Maximum gradient for the mean of the actor
        double gradStdMax, // Maximum gradient for the standard deviation of the actor
        double tdMax, // Maximum TD error for training
        double advMax // Maximum advantage for training
) {

    public static final int N_KERNELS_Y = 6;
    public static final int N_KERNELS_SPD = 6;
    public static final double LEARNING_RATE_CRITIC = 1e-3;
    public static final double LEARNING_RATE_ACTOR = 1e-3;
    public static final double INIT_WEIGHT_LOG_STD = 0.25d;  //set so std starts at approx 5
    public static final int BATCH_SIZE = 20;
    public static final int N_EPOCHS = 5;
    public static final double REL_SIGMA = 0.75;
    public static final double GRAD_MEAN_MAX = 0.1;
    public static final double GRAD_STD_MAX = 0.01;  //small => std decreases slowly
    public static final double TD_MAX = 10d;
    public static final double ADV_MAX = 10d;

    public static AgentParameters newDefault(LunarParameters ep) {
        return of(ep, N_KERNELS_Y, N_KERNELS_SPD);
    }

    public static AgentParameters of(LunarParameters ep, int nKernelsY, int nKernelsSpd) {
        double sigmaY = getSigmaY(ep, nKernelsY);
        double sigmaSpd = getSigmaSpd(ep, nKernelsSpd);
        return AgentParameters.builder().
                nKernelsY(nKernelsY).
                nKernelsSpeed(nKernelsSpd).
                gammas(new double[]{gamma(sigmaY),gamma(sigmaSpd)}).
                initWeightLogStd(INIT_WEIGHT_LOG_STD).
                learningRateCritic(LEARNING_RATE_CRITIC).
                learningRateActor(LEARNING_RATE_ACTOR).
                batchSize(BATCH_SIZE).
                nEpochs(N_EPOCHS).
                gradMeanMax(GRAD_MEAN_MAX).gradStdMax(GRAD_STD_MAX).
                tdMax(TD_MAX).advMax(ADV_MAX).
                build();
    }


    public double clipAdvantage(double adv0) {
        return MyMathUtils.clip(adv0, -advMax, advMax);
    }

    public double clipTdError(double e0) {
        return MyMathUtils.clip(e0, -tdMax(), tdMax());
    }

    private static double getSigmaSpd(LunarParameters ep, int nKernelsSpd) {
        return REL_SIGMA * (ep.spdMax() - -ep.spdMax()) / (nKernelsSpd-1);
    }

    private static double getSigmaY(LunarParameters ep, int nKernelsY1) {
        return REL_SIGMA * (ep.yMax() - ep.ySurface()) / (nKernelsY1-1);
    }

    /**
     * Sigma is like the radius of a circle, controlling the size of the kernel.
     * Gamma is like the "stickiness" of the kernel, controlling how quickly it
     * decays as you move away from the center.
     */

    static double gamma(double sigma) {
        return 1 / (2 * sigma * sigma);
    }

}
