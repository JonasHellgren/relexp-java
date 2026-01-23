package chapters.ch10.cannon.domain.agent;

import core.foundation.gadget.math.MeanAndStdMemoryParameters;
import core.foundation.util.math.MathUtil;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the memory of a cannon agent, storing the z-values (zm, zs) that correspond to the mean and standard deviation of the policy.
 * The mean and standard deviation are calculated as (zm, exp(zs)).
 * <p>
 * Memory contains z values  (zm,zs)
 * (mean,std)=(zm,exp(zs))
 *
 * Using the log-standard deviation, zs, instead of the standard deviation, σ, has several advantages:
 *
 * Ensures positivity: The exponential function ensures that the standard deviation is always positive, which
 * is a necessary property for a standard deviation.
 * Improves numerical stability: The log-standard deviation can be more numerically stable than
 * the standard deviation, especially when the standard deviation is close to zero.
 */

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryCannon {

    private MeanAndStdMemoryParameters memoryParameters;
    private AgentParametersCannon parameters;

    public static MemoryCannon of(AgentParametersCannon parameters) {
        return new MemoryCannon(parameters.zInit(), parameters);
    }

    public double mean() {
        return memoryParameters.zMean();
    }

    public double std() {
        return Math.exp(memoryParameters.zStd());

    }

    /**
     * Updates the memory parameters by adding the given gradient and learning rate.
     * The new z-values are calculated as the sum of the current z-values and the product of the gradient and learning rate.
     *
     * @param gradLog the gradient of the log-likelihood
     * @param alphaG  the learning rate
     */

    public void add(GradientMeanAndLogStd gradLog, double alphaG) {
        double zmNew = memoryParameters.zMean() + gradLog.mean() * alphaG;
        double zsNew = memoryParameters.zStd() + gradLog.std() * alphaG;
        memoryParameters = MeanAndStdMemoryParameters.of(zmNew, zsNew);
    }

    /**
     * Clips the memory parameters to ensure they remain within the valid range.
     * The clipping is done using the agent's minimum and maximum mean and standard deviation values.
     */
    public void clip() {
        var msMin = parameters.meanAndStdMin();
        var msMax = parameters.meanAndStdMax();
        double zmNew = MathUtil.clip(memoryParameters.zMean(), msMin.mean(), msMax.mean());
        double zsNew = MathUtil.clip(memoryParameters.zStd(), parameters.minZStd(), parameters.maxZStd());
        memoryParameters = MeanAndStdMemoryParameters.of(zmNew, zsNew);
    }
}
