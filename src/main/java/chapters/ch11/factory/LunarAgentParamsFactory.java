package chapters.ch11.factory;

import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.param.LunarParameters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LunarAgentParamsFactory {

    public static final double REL_SIGMA = 0.75;
    public static final int N_KERNELS_Y = 6;
    public static final int N_KERNELS_SPD = 6;
    public static final double LEARNING_RATE_CRITIC = 1e-3;
    public static final double LEARNING_RATE_ACTOR = 1e-3;
    public static final double INIT_WEIGHT_LOG_STD = 0.25d;  //set so std starts at approx 5
    public static final int N_EPOCHS = 20;  //5
    public static final double GRAD_MEAN_MAX = 0.1;
    public static final double GRAD_STD_MAX = 0.01;  //small => std decreases slowly
    public static final double TD_MAX = 10d;
    public static final double ADV_MAX = 10d;

    public static AgentParameters newDefault(LunarParameters ep) {
        double sigmaY = getSigmaY(ep, N_KERNELS_Y);
        double sigmaSpd = getSigmaSpd(ep, N_KERNELS_SPD);
        return AgentParameters.builder().
                nKernelsY(N_KERNELS_Y).
                nKernelsSpeed(N_KERNELS_SPD).
                gammas(new double[]{gamma(sigmaY),gamma(sigmaSpd)}).
                initWeightLogStd(INIT_WEIGHT_LOG_STD).
                learningRateCritic(LEARNING_RATE_CRITIC).
                learningRateActor(LEARNING_RATE_ACTOR).
                nEpochs(N_EPOCHS).
                gradMeanMax(GRAD_MEAN_MAX).gradStdMax(GRAD_STD_MAX).
                tdMax(TD_MAX).advMax(ADV_MAX).
                build();
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
