package chapters.ch10.cannon.domain.agent;

import core.foundation.gadget.math.MeanAndStd;
import core.foundation.gadget.normal_distribution.NormDistributionSampler;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Represents an agent for the cannon domain.
 * This class is responsible for choosing actions, updating memory, and providing the mean and standard deviation of the policy.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentCannon {

    MemoryCannon memory;
    NormDistributionSampler sampler;
    AgentParametersCannon parameters;

    public static AgentCannon of(AgentParametersCannon parameters) {
        return new AgentCannon(
                MemoryCannon.of(parameters),
                NormDistributionSampler.create(),
                parameters);
    }

    /**
     * Chooses an action from the policy by sampling from the normal distribution.
     *
     * @return the chosen action
     */
    public double chooseAction() {
        return sampler.sampleFromNormDistribution(memory.mean(),memory.std());
    }

    /**
     * Updates the memory of the agent based on the given gradient and return.
     *
     * @param learningRate the learning rate of the update
     * @param returnAtT the return at time t
     * @param gradLog the gradient of the log policy
     */
    public void updateMemory(double learningRate,
                             double returnAtT,
                             GradientMeanAndLogStd gradLog) {
        var gradClipped=gradLog.clip(parameters.gradzMeanMax(),parameters.gradzStdMax());
        memory.add(gradClipped,learningRate * returnAtT);
        memory.clip();
    }

    public MeanAndStd meanAndStd() {
        return MeanAndStd.of(memory.mean(),memory.std());
    }


}
