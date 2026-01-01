package core.gridrl;

import lombok.Builder;

/**
 * Represents the return value of a step in a grid environment.
 * It contains the new state, whether the step resulted in a failure or termination,
 * and the reward for the step.
 */
@Builder
public record StepReturnGrid(
        StateGrid sNext,
        boolean isFail,
        boolean isTerminal,
        double reward
) {


    public static StepReturnGrid of(StateGrid stateNew, boolean isFail, boolean isTerminal, double reward) {
        return new StepReturnGrid(stateNew, isFail, isTerminal, reward);
    }

    public static StepReturnGrid ofNotTerminal(StateGrid stateNew, double reward) {
        return new StepReturnGrid(stateNew, false, false, reward);
    }

    public static StepReturnGrid ofTerminalFail(StateGrid stateNew, double reward) {
        return new StepReturnGrid(stateNew, true, true, reward);
    }



}
