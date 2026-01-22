package chapters.ch10.bandit.domain.agent;

import chapters.ch10.bandit.domain.environment.ActionBandit;
import core.foundation.util.collections.ArrayUtil;
import core.foundation.gadget.math.SoftMax;
import core.foundation.util.rand.RandUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Represents a bandit agent that uses policy gradient methods to learn.
 * This agent maintains a memory of the policy parameters and updates them based on the return at each time step.
 *
 * updateMemory:
 * θ←θ+α∙G(t)∙∇log⁡π(a(t)│s(t),θ)
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentBandit {

    private final MemoryBandit memory;

    public static AgentBandit of(AgentParametersBandit parameters) {
        return new AgentBandit(MemoryBandit.of(parameters));
    }

    /**
     * Chooses an action based on the current policy.
     * @return the chosen action
     */
    public ActionBandit chooseAction() {
        int indexAction=(RandUtil.randomNumberBetweenZeroAndOne()< actionProbabilities()[0])
                ?ActionBandit.LEFT.getIndex()
                :ActionBandit.RIGHT.getIndex();
        return  ActionBandit.fromIndex(indexAction);
    }


    /**
     * Updates the agent's memory based on the return at the current time step.
     * @param learningRate the learning rate of the agent
     * @param returnAtT the return at the current time step
     * @param gradLog the gradient of the log policy
     */
    public void updateMemory(double learningRate, double returnAtT, double[] gradLog) {
        memory.add(ArrayUtil.multiplyWithValue(gradLog,learningRate * returnAtT));
    }

    /**
     * Returns the action probabilities based on the current policy.
     * @return the action probabilities  (prob LEFT, prob RIGHT)
     */

    public double[] actionProbabilities() {
        return SoftMax.softmax(memory.getMemoryParameters());
    }

}
