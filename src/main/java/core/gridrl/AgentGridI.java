package core.gridrl;


/**
 * Interface for agents in a grid environment.
 * This interface defines the methods that an agent must implement to interact with the environment.
 */
public interface AgentGridI {
    /**
     * Choose an action based on the current state and a probability of random action.
     *
     * @param state      the current state of the environment
     * @param probRandom the probability of choosing a random action
     * @return the chosen action
     */
    ActionGrid chooseAction(StateGrid state, double probRandom);

    /**
     * Choose an action based on the current state without exploration (i.e. always choose the greedy action).
     *
     * @param state the current state of the environment
     * @return the chosen action
     */
    ActionGrid chooseActionNoExploration(StateGrid state);

    /**
     * Update the agent's memory based on a new experience.
     *
     * @param experience   the new experience
     * @param learningRate the learning rate for updating the memory
     */
    void fitMemory(ExperienceGrid experience, double learningRate);

    /**
     * Calculate the target value for a given experience.
     *
     * @param e the experience
     * @return the target value
     */
    double calculateValueTarget(ExperienceGrid e);

    /**
     * Read the value of a given state.
     *
     * @param state the state
     * @return the value of the state
     */
    double readValue(StateGrid state);

    /**
     * Read the value of a given state-action pair.
     *
     * @param state  the state
     * @param action the action
     * @return the value of the state-action pair
     */
    double read(StateGrid state, ActionGrid action);

    /**
     * Get the number of items in the agent's memory.
     *
     * @return the number of items in the memory
     */
    int getNumberOfItemsInMemory();

    /**
     * Get the agent's parameters.
     *
     * @return the agent's parameters
     */
    AgentGridParameters getAgentParameters();

}
