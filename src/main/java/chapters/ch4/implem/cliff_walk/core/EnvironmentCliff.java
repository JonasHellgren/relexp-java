package chapters.ch4.implem.cliff_walk.core;

import core.gridrl.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Implementation of the EnvironmentGridI interface for the Cliff Walk environment.
 */
@AllArgsConstructor
@Getter
public class EnvironmentCliff implements EnvironmentGridI {

    public static final String NAME = "Cliff";
    private final EnvironmentGridParametersI parameters;

    public static EnvironmentCliff of(EnvironmentGridParametersI gridParameters) {
        return new EnvironmentCliff(gridParameters);
    }

    @Override
    public String name() {
        return NAME;
    }

    /**
     * Takes a step in the environment from the given state and action.
     *
     * @param s Current state.
     * @param a Action to take.
     * @return The result of taking the action from the current state.
     */
    @Override
    public StepReturnGrid step(StateGrid s, ActionGrid a) {
        parameters.validateStateAndAction(s,a);
        var sNext = getNextState(s, a);
        var isFail = parameters.isFail(sNext);
        var isTerminal = parameters.isTerminalNonFail(sNext) || isFail;
        var reward = getReward(sNext,isTerminal);
        return StepReturnGrid.builder()
                .sNext(sNext).reward(reward)
                .isFail(isFail).isTerminal(isTerminal)
                .build();
    }

    private StateGrid getNextState(StateGrid s, ActionGrid a) {
        return s.ofApplyingAction(a).clip(parameters);
    }

    private double getReward(StateGrid sNext, boolean isTerminal) {
        double rTerminal=isTerminal ? parameters.rewardAtTerminalPos(sNext):0;
        return rTerminal+parameters.rewardMove();
    }


}
