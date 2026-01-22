package chapters.ch8.domain.agent.core;

import chapters.ch8.domain.agent.memory.AgentMemory;
import chapters.ch8.domain.agent.memory.StateActionParking;
import chapters.ch8.domain.agent.param.AgentParkingParameters;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.StateParking;
import core.foundation.util.rand.RandUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Comparator;
import java.util.Map;

/**
 * Represents an agent in a parking environment.
 * The agent has a memory and parameters that define its behavior.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentParking {

    private final AgentParkingParameters agentParameters;
    @Setter
    @Getter
    private AgentMemory memory;

    public static AgentParking of(AgentParkingParameters agentParameters) {
        return new AgentParking(agentParameters, AgentMemory.of(agentParameters));
    }

    /**
     * Chooses an action for the given state, with a probability of random exploration.
     * If the random number is less than the probability, a random action is chosen.
     * Otherwise, the action with the highest value is chosen.
     *
     * @param s the current state
     * @param probRandom the probability of random exploration
     * @return the chosen action
     */
    public ActionParking chooseAction(StateParking s, double probRandom) {
        return RandUtil.randomNumberBetweenZeroAndOne() < probRandom
                ? ActionParking.random()
                : chooseActionNoExploration(s);
    }

    /**
     * Chooses the action with the highest value for the given state, without exploration.
     * The action values are read from the memory.
     *
     * @param s the current state
     * @return the chosen action
     */
    public ActionParking chooseActionNoExploration(StateParking s) {
        var aMap = memory.readActionValues(s);
        return aMap.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow();
    }

    /**
     * Updates the agent's memory with the given experience and learning rate.
     * The experience includes the state, action, and reward.
     * The learning rate determines how much the memory is updated.
     *
     * @param exp the experience
     * @param learningRate the learning rate
     * @return the updated value
     */
    public double fitMemory(ExperienceParking exp, double learningRate) {
        double valueSnewAnew = memory.read(StateActionParking.of(exp.stateNew(), exp.actionNew()));
        double qTar = exp.deltaReward() + agentParameters.gamma() * valueSnewAnew;
        var sa = StateActionParking.ofExp(exp);
        return memory.fit(sa, qTar, learningRate);
    }

    public double value(StateParking s) {
        var actionBest = chooseActionNoExploration(s);
        return memory.read(StateActionParking.of(s, actionBest));
    }

}


