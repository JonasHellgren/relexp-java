package chapters.ch3.implem.splitting_path_problem;

import com.google.common.base.Preconditions;
import core.foundation.gadget.pos.PosXyInt;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.HashSet;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InformerSplitting {

    EnvironmentParametersSplitting parameters;

    public static InformerSplitting create(EnvironmentParametersSplitting parameters) {
        return new InformerSplitting(parameters);
    }

    public void validateStateAndAction(StateGrid s, ActionGrid a) {
        Preconditions.checkArgument(isValidState(s), "invalid state=" + s);
        Preconditions.checkArgument(isValidAction(a), "invalid action=" + a);
    }

    public String environmentName() {
        return "SplittingPath";
    }

    public boolean isTerminalNonFail(StateGrid state) {
        return parameters.getTerminalNonFailsStates().contains(state);
    }

    public boolean isFail(StateGrid state) {
        return parameters.getFailStates().contains(state);
    }

    public boolean isWall(StateGrid state) {
        return parameters.getWallStates().contains(state);
    }

    public Double rewardAtTerminalPos(StateGrid state) {
        validateTerminalState(state);
        return (isFail(state)) ? 0 : parameters.getRewardAtGoalPos().apply(state);
    }

    public Double rewardMove() {
        return parameters.getRewardMove();
    }


    public boolean isValidState(StateGrid state) {
        return state.x() >= parameters.getPosXMinMax().getFirst() && state.x() <= parameters.getPosXMinMax().getSecond() &&
                state.y() >= parameters.getPosYMinMax().getFirst() && state.y() <= parameters.getPosYMinMax().getSecond();
    }

    public boolean isValidAction(ActionGrid action) {
        return parameters.getValidActions().contains(action);
    }


    public boolean isAtSplit(StateGrid s) {
        return parameters.getSplitState().contains(s);
    }

    public HashSet<StateGrid> getAllStates() {
        var allStates = new HashSet<>(parameters.getStatesExceptSplit());
        allStates.add(parameters.getSplitState().iterator().next());
        return allStates;
    }

    public boolean isTerminal(StateGrid state) {
        return isTerminalNonFail(state) || isFail(state);
    }

    void validateTerminalState(StateGrid state) {
        boolean terminal = isTerminalNonFail(state) || isFail(state);
        Preconditions.checkArgument(terminal, "invalid state="+ state +", shall be terminal");
    }

    public PosXyInt xyMin() {
        return PosXyInt.of(parameters.getPosXMinMax().getFirst(), parameters.getPosYMinMax().getFirst());
    }

    public PosXyInt xyMax() {
        return PosXyInt.of(parameters.getPosXMinMax().getSecond(), parameters.getPosYMinMax().getSecond());
    }

}
