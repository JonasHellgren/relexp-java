package chapters.ch14.domain.environment;

import lombok.Builder;

/**
 * Represents the return value of a step in an environment.
 * This record class contains the new state, whether the step resulted in a failure or termination,
 * and the reward for the step.
 *
 * @param <S> the type of the state
 */
@Builder
public record StepReturn<S>(
    S stateNew,
    boolean isTerminal,
    boolean isFail,
    double reward) {


    public static <S> StepReturn<S> empty() {
        return StepReturn.<S>builder()
                .stateNew(null)
                .isTerminal(false)
                .isFail(false)
                .reward(0.0)
                .build();
    }


    @Override
    public String toString() {
        return "StepReturn{" +
                "stateNew=" + stateNew +
                System.lineSeparator() +
                ", isTerminal=" + isTerminal +
                ", isFail=" + isFail +
                ", reward=" + reward +
                '}';
    }

}
