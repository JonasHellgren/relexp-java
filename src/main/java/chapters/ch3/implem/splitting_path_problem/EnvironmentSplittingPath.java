package chapters.ch3.implem.splitting_path_problem;

import core.gridrl.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EnvironmentSplittingPath implements EnvironmentGridI {

    public static final String NAME="Splitting";
    private final EnvironmentGridParametersI parameters;

    public static EnvironmentSplittingPath of(EnvironmentGridParametersI parameters) {
        return new EnvironmentSplittingPath(parameters);
    }

    public String name() {
        return NAME;
    }

    /**
     * Takes a step in the environment, given a state and an action.
     *
     * @param s the current state of the environment
     * @param a the action to take
     * @return the result of taking the action in the current state
     */

    public StepReturnGrid step(StateGrid s, ActionGrid a) {
        parameters.validateStateAndAction(s,a);
        var sNext = getNextState(s, a);
        var isFail = parameters.isFail(sNext);
        var isTerminal = parameters.isTerminalNonFail(sNext) || isFail;
        var reward = calculateReward(sNext,isTerminal);
        return StepReturnGrid.builder()
                .sNext(sNext)
                .reward(reward)
                .isFail(isFail)
                .isTerminal(isTerminal)
                .build();
    }

    private StateGrid getNextState(StateGrid s, ActionGrid a) {
        var sAfterActionApplied = s.ofApplyingAction(a).clip(parameters);
        return stateNotPositionedAtWall(s, sAfterActionApplied);
    }

    private StateGrid stateNotPositionedAtWall(StateGrid s, StateGrid sAfterActionApplied) {
        return parameters.isWall(sAfterActionApplied) ? s : sAfterActionApplied;
    }

    private double calculateReward(StateGrid sNext, boolean isTerminal) {
        double rTerminal=isTerminal ? parameters.rewardAtTerminalPos(sNext):0;
        return rTerminal+parameters.rewardMove();
    }

}
