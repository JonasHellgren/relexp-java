package chapters.ch14.domain.environment;

/**
 * This interface represents a generic environment in a MCTS setting.
 * It provides a method to take a step in the environment, given a current state and an action.
 *
 * @param <S> The type of the state in the environment.
 * @param <A> The type of the action in the environment.
 */
public interface EnvironmentI<S, A> {

    
    
    /**
     * Takes a step in the environment, given a current state and an action.
     * This method returns a StepReturnI object, which contains information about the outcome of the step,
     * including the new state, reward, and whether the step was terminal.
     *
     * @param state The current state of the environment.
     * @param action The action to take in the environment.
     * @return A StepReturnI object containing information about the outcome of the step.
     */
    StepReturn<S> step(S state, A action);

}
