package chapters.ch13.domain.environment;

/**
 * Represents the return value of a step in an environment.
 * This record class contains the new state, whether the step resulted in a failure or termination,
 * and the reward for the step.
 *
 * @param <S> the type of the state
 */
public record StepReturnI<S>(
    S stateNew,
    boolean isTerminal,
    boolean isFail,
    double reward) {

}
