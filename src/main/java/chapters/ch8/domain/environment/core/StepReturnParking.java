package chapters.ch8.domain.environment.core;

import lombok.Builder;

/**
 * Represents the return value of a step in the parking environment.
 * It contains the new state, whether a park action was accepted, whether a vehicle is departing,
 * whether the step resulted in a terminal state, and the reward for the step.
 *
 * Used by the EnvironmentParking class in its step method to return the result of a step.
 */
@Builder
public record StepReturnParking(
        StateParking stateNew,
        boolean isPark,
        boolean isDeparting,
        boolean isTerminal,
        double reward
) {

    public static StepReturnParking of(EnvironmentParking.NewStateResult result,
                                       boolean isTerminal,
                                       double reward) {
        return StepReturnParking.builder()
                .stateNew(result.state())
                .isPark(result.isPark())
                .isDeparting(result.isDeparting())
                .isTerminal(isTerminal)
                .reward(reward)
                .build();
    }

}
