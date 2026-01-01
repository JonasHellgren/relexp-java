package chapters.ch4.domain.agent;

import chapters.ch4.domain.helper.ActionSelectorGrid;
import chapters.ch4.domain.memory.MemoryGrid;
import chapters.ch4.domain.param.AgentGridParameters;
import chapters.ch4.domain.trainer.core.ExperienceGrid;
import core.foundation.gadget.training.ValueCalculator;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Q-Learning agent implementation.
 * This agent uses a memory grid to store state-action values and an action selector to choose actions.
 */

@Getter
@AllArgsConstructor
public class AgentQLearningGrid implements AgentGridI {

    private static final double ZERO_PROBABILITY = 0;
    private final AgentGridParameters agentParameters;
    private final MemoryGrid memory;
    private final ActionSelectorGrid actionSelector;

    public static AgentQLearningGrid of(AgentGridParameters agentParameters, EnvironmentGridParametersI gridParameters) {
        return new AgentQLearningGrid(
                agentParameters,
                MemoryGrid.of(agentParameters, gridParameters),
                ActionSelectorGrid.of(gridParameters));
    }
    /**
     * Chooses an action for the given state using epsilon-greedy strategy.
     *
     * @param state  current state
     * @param probRandom  probability of choosing a random action
     * @return  chosen action
     */
    @Override
    public ActionGrid chooseAction(StateGrid state, double probRandom) {
        return actionSelector.chooseActionEpsilonGreedy(state, probRandom, memory);
    }

    /**
     * Chooses an action for the given state without exploration.
     *
     * @param state  current state
     * @return  chosen action
     */
    @Override
    public ActionGrid chooseActionNoExploration(StateGrid state) {
        return chooseAction(state, ZERO_PROBABILITY);
    }

    /**
     * Updates the memory with the given experience.
     * Q(s, a) ←  Q(s, a) + startAndEndLearningRate * (r + γ * max(Q(s', a')) - Q(s, a)) =
     * Q(s, a) + startAndEndLearningRate*valueTarget
     *
     * @param experience  (experience)
     * @param learningRate  (learning rate)
     */

    @Override
    public void fitMemory(ExperienceGrid experience , double learningRate) {
        memory.fit(experience.state(),
                experience.action(),
                calculateValueTarget(experience ),
                learningRate);
    }

    @Override
    public double calculateValueTarget(ExperienceGrid e) {
        double qNextMax = memory.getValueOfBestAction(e.stateNext());
        return ValueCalculator.calculateTargetValue(
                e.isTransitionToTerminal(),
                e.reward(),
                qNextMax,
                agentParameters.discountFactor());
    }

    /**
     * Reads the value of the best action for the given state.
     *
     * @param state  current state
     * @return  value of the best action
     */
    @Override
    public double readValue(StateGrid state) {
        return memory.getValueOfBestAction(state);
    }

    /**
     * Reads the value of the given state-action pair.
     *
     * @param state  current state
     * @param action  action
     * @return  value of the state-action pair
     */
    @Override
    public double read(StateGrid state, ActionGrid action) {
        return memory.read(state, action);
    }

    @Override
    public int getNumberOfItemsInMemory() {
        return memory.getNumberOfFittedStateActions();
    }

    @Override
    public String toString() {
        return memory.toString();
    }

}
