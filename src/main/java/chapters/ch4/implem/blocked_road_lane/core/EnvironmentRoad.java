package chapters.ch4.implem.blocked_road_lane.core;

import com.google.common.base.Preconditions;
import core.foundation.util.cond.Conditionals;
import core.gridrl.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Implementation of the Road environment.
 * This class provides the logic for stepping through the environment, given a state and an action.
 */
@AllArgsConstructor
@Getter
public class EnvironmentRoad implements EnvironmentGridI {

    public static final String NAME = "Road";
    private final EnvironmentGridParametersI parameters;

    public static EnvironmentRoad of(EnvironmentGridParametersI gridParameters) {
        Preconditions.checkArgument(gridParameters.environmentName().equals(NAME),
                "grid parameters not matching environment");
        return new EnvironmentRoad(gridParameters);
    }

    @Override
    public String name() {
        return NAME;
    }

    /**
     * Takes a step in the environment, given the current state and action.
     *
     * @param s the current state
     * @param a the action to take
     * @return the result of taking the step
     */
    @Override
    public StepReturnGrid step(StateGrid s, ActionGrid a) {
        parameters.validateStateAndAction(s, a);
        var sNext = getNextState(s, a);
        var isFail = parameters.isFail(sNext);
        var isTerminal = parameters.isTerminalNonFail(sNext) || isFail;
        var isMove = isMovingSouthOrNorth(a);
        var reward = getReward(sNext, isTerminal, isMove);
        return StepReturnGrid.builder()
                .sNext(sNext).reward(reward)
                .isFail(isFail).isTerminal(isTerminal)
                .build();
    }

    private StateGrid getNextState(StateGrid s, ActionGrid a) {
        var xNext = s.x() + 1;
        var yNext = s.y() + a.deltaY();
        return StateGrid.of(xNext, yNext).clip(parameters);
    }

    private double getReward(StateGrid sNext, boolean isTerminal, boolean isMove) {
        return Conditionals.numIfTrueElseZero.apply(isTerminal, parameters.rewardAtTerminalPos(sNext)) +
                Conditionals.numIfTrueElseZero.apply(isMove, parameters.rewardMove());
    }

    private boolean isMovingSouthOrNorth(ActionGrid a) {
        return a.equals(ActionGrid.N) || a.equals(ActionGrid.S);
    }

}
