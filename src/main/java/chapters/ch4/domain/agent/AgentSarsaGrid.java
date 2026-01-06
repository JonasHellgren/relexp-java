package chapters.ch4.domain.agent;

import core.gridrl.ActionSelectorGrid;
import core.gridrl.StateActionMemoryGrid;
import core.gridrl.AgentGridParameters;
import core.gridrl.InformerGridParamsI;
import core.gridrl.ExperienceGrid;
import com.google.common.base.Preconditions;
import core.foundation.gadget.training.ValueCalculator;
import core.gridrl.ActionGrid;
import core.gridrl.AgentGridI;
import core.gridrl.StateGrid;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Implementation of the Sarsa algorithm for grid-based environments.
 */
@Getter
@AllArgsConstructor
public class AgentSarsaGrid implements AgentGridI {

    private  static final int ZERO_PROBABILITY = 0;
    private final AgentGridParameters agentParameters;
    private final StateActionMemoryGrid memory;
    private final ActionSelectorGrid actionSelector;

    public static AgentSarsaGrid of(AgentGridParameters agentParameters, InformerGridParamsI informer) {
        return new AgentSarsaGrid(
                agentParameters,
                StateActionMemoryGrid.of(agentParameters, informer),
                ActionSelectorGrid.of(informer));
    }

    /**
     * Chooses an action based on the given state and probability of random action.
     *
     * @param state      current state
     * @param probRandom probability of random action
     * @return chosen action
     */
    @Override
    public ActionGrid chooseAction(StateGrid state, double probRandom) {
        return actionSelector.chooseActionEpsilonGreedy(state, probRandom, memory);
    }

    /**
     * Chooses an action without exploration (i.e., with zero probability of random action).
     *
     * @param state current state
     * @return chosen action
     */
    @Override
    public ActionGrid chooseActionNoExploration(StateGrid state) {
        return chooseAction(state, ZERO_PROBABILITY);
    }

    /**
     * Updates the agent's memory based on the given experience and learning rate.
     * Q(s, a) ←  Q(s, a) + α * (r + γ*(Q(s', a')) - Q(s, a))
     *
     * @param e (experience)
     * @param learningRate (learning rate)
     */
    @Override
    public void fitMemory(ExperienceGrid e, double learningRate) {
        memory.fit(e.state(), e.action(), calculateValueTarget(e), learningRate);
    }

    @Override
    public double calculateValueTarget(ExperienceGrid experience ) {
        Preconditions.checkArgument(experience.actionNext().isPresent(), "Action next is not present");
        double qNext = memory.read(experience.stateNext(), experience .actionNext().orElseThrow());
        return ValueCalculator.calculateTargetValue(
                experience .isTransitionToTerminal(),
                experience .reward(),
                qNext,
                agentParameters.discountFactor());
    }

    /**
     * Reads the value of the best action for the given state.
     *
     * @param state current state
     * @return value of best action
     */
    @Override
    public double readValue(StateGrid state) {
        return memory.getValueOfBestAction(state);
    }

    /**
     * Reads the value of the given state-action pair.
     *
     * @param state current state
     * @param action current action
     * @return value of state-action pair
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
