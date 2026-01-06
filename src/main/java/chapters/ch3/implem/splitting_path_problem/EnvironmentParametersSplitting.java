package chapters.ch3.implem.splitting_path_problem;

import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.Builder;
import lombok.With;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Builder
@With
public record EnvironmentParametersSplitting (

     Pair<Integer, Integer> posXMinMax,
     Pair<Integer, Integer> posYMinMax,
     List<ActionGrid> validActions,
     Set<StateGrid> terminalNonFailsStates,
     Set<StateGrid> failStates,
     Set<StateGrid> wallStates,
     Set<StateGrid> splitState,
     Set<StateGrid> statesExceptSplit,
     Function<StateGrid, Double> rewardAtGoalPos,
     Double rewardAtFailPos,
     Double rewardMove)

{}
/*

    //TODO NEDAN METODER VECK CH6

    @Override
    public void validateStateAndAction(StateGrid s, ActionGrid a) {
        Preconditions.checkArgument(isValidState(s), "invalid state=" + s);
        Preconditions.checkArgument(isValidAction(a), "invalid action=" + a);
    }

    @Override
    public String environmentName() {
        return "SplittingPath";
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
        return (isFail(state)) ? 0 : rewardAtGoalPos.apply(state);
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


    public boolean isAtSplit(StateGrid s) {
        return splitState.contains(s);
    }

    public  HashSet<StateGrid> getAllStates() {
        var allStates = new HashSet<>(getStatesExceptSplit());
        allStates.add(getSplitState().iterator().next());
        return allStates;
    }

*/

