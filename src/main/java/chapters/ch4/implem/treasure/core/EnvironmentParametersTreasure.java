package chapters.ch4.implem.treasure.core;

import com.google.common.base.Preconditions;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.function.Function;


/**
 * Represents the parameters for the Treasure Hunt environment.
 * This class implements the GridParametersI interface and provides the necessary
 * methods for validating states and actions, determining terminal and fail states,
 * and calculating rewards.
 */
@Getter
@Builder
@Setter
public class EnvironmentParametersTreasure implements EnvironmentGridParametersI {

    public static final String NAME="treasure";

    private Pair<Integer, Integer> posXMinMax;
    private Pair<Integer, Integer> posYMinMax;
    private List<ActionGrid> validActions;
    private Set<StateGrid> terminalNonFailsStates;
    private Set<StateGrid> failStates;
    private Set<StateGrid> wallStates;
    private Double rewardAtFailPos;
    private Function<StateGrid, Double> rewardAtGoalPos;
    private Double rewardMove;

    @Override
    public void validateStateAndAction(StateGrid s, ActionGrid a) {
        Preconditions.checkArgument(isValidState(s), "invalid state=" + s);
        Preconditions.checkArgument(isValidAction(a), "invalid action=" + a);
    }

    @Override
    public String environmentName() {
        return NAME;
    }

    @Override
    public boolean isTerminalNonFail(StateGrid state) {
        return terminalNonFailsStates.contains(state);
    }

    @Override
    public boolean isFail(StateGrid state) {
        return failStates.contains(state);
    }

    @Override
    public boolean isWall(StateGrid state) {
        return wallStates.contains(state);
    }

    @Override
    public Double rewardAtTerminalPos(StateGrid state) {
        validateTerminalState(state);
        return (isFail(state)) ? rewardAtFailPos : rewardAtGoalPos.apply(state);
    }

    @Override
    public Double rewardMove() {
        return rewardMove;
    }


    @Override
    public boolean isValidState(StateGrid state) {
        return state.x() >= posXMinMax.getFirst() && state.x() <= posXMinMax.getSecond() &&
                state.y() >= posYMinMax.getFirst() && state.y() <= posYMinMax.getSecond();
    }

    @Override
    public boolean isValidAction(ActionGrid action) {
        return validActions.contains(action);
    }


}
