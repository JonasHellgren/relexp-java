package chapters.ch5.domain.environment;

public interface EnvironmentMcI {

    /**
     * Returns the name of the environment.
     *
     * @return the name of the environment
     */
    String name();

    /**
     * Takes a step in the environment.
     *
     * @param state the current state of the environment
     * @param action the action to take in the environment
     * @return the return value of the step, including the new state, whether the
     * step resulted in a failure or termination, and the reward for the step
     */
    StepReturnMc step(StateMcI state, ActionMcI action);

}
