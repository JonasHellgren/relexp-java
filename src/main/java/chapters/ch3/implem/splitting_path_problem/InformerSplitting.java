package chapters.ch3.implem.splitting_path_problem;

import chapters.ch4.domain.param.InformerGridParamsI;
import com.google.common.base.Preconditions;
import core.foundation.gadget.pos.PosXyInt;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

import java.util.HashSet;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InformerSplitting implements InformerGridParamsI {

    EnvironmentParametersSplitting parameters;

    public static InformerSplitting create(EnvironmentParametersSplitting parameters) {
        return new InformerSplitting(parameters);
    }

    @Override
    public void validateStateAndAction(StateGrid s, ActionGrid a) {
        Preconditions.checkArgument(isValidState(s), "invalid state=" + s);
        Preconditions.checkArgument(isValidAction(a), "invalid action=" + a);
    }

    public String environmentName() {
        return "SplittingPath";
    }

    @Override
    public Pair<Integer, Integer> getPosXMinMax() {
        return parameters.posXMinMax();
    }

    @Override
    public Pair<Integer, Integer> getPosYMinMax() {
        return parameters.posYMinMax();
    }


    @Override
    public List<ActionGrid> getValidActions() {
        return parameters.validActions();
    }

    @Override
    public boolean isTerminalNonFail(StateGrid state) {
        return parameters.terminalNonFailsStates().contains(state);
    }

    @Override
    public boolean isFail(StateGrid state) {
        return parameters.failStates().contains(state);
    }

    @Override
    public boolean isWall(StateGrid state) {
        return parameters.wallStates().contains(state);
    }

    @Override
    public Double rewardAtTerminalPos(StateGrid state) {
        validateTerminalState(state);
        return (isFail(state)) ? 0 : parameters.rewardAtGoalPos().apply(state);
    }

    @Override
    public Double rewardMove() {
        return parameters.rewardMove();
    }

    @Override
    public boolean isValidState(StateGrid state) {
        return state.x() >= parameters.posXMinMax().getFirst() && state.x() <= parameters.posXMinMax().getSecond() &&
                state.y() >= parameters.posYMinMax().getFirst() && state.y() <= parameters.posYMinMax().getSecond();
    }

    @Override
    public boolean isValidAction(ActionGrid action) {
        return parameters.validActions().contains(action);
    }

    @Override
    public boolean isTerminal(StateGrid state) {
        return isTerminalNonFail(state) || isFail(state);
    }

    @Override
    public void validateTerminalState(StateGrid state) {
        boolean terminal = isTerminalNonFail(state) || isFail(state);
        Preconditions.checkArgument(terminal, "invalid state="+ state +", shall be terminal");
    }

    @Override
    public PosXyInt xyMin() {
        return PosXyInt.of(parameters.posXMinMax().getFirst(), parameters.posYMinMax().getFirst());
    }

    @Override
    public PosXyInt xyMax() {
        return PosXyInt.of(parameters.posXMinMax().getSecond(), parameters.posYMinMax().getSecond());
    }


    public boolean isAtSplit(StateGrid s) {
        return parameters.splitState().contains(s);
    }

    public HashSet<StateGrid> getAllStates() {
        var allStates = new HashSet<>(parameters.statesExceptSplit());
        allStates.add(parameters.splitState().iterator().next());
        return allStates;
    }


}
