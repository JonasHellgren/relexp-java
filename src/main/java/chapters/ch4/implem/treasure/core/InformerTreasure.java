package chapters.ch4.implem.treasure.core;

import core.gridrl.InformerGridParamsI;
import com.google.common.base.Preconditions;
import core.foundation.gadget.pos.PosXyInt;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InformerTreasure implements InformerGridParamsI {

    EnvironmentParametersTreasure par;

    public static InformerTreasure create(EnvironmentParametersTreasure parameters) {
        return new InformerTreasure(parameters);
    }


    @Override
    public void validateStateAndAction(StateGrid s, ActionGrid a) {
        Preconditions.checkArgument(isValidState(s), "invalid state=" + s);
        Preconditions.checkArgument(isValidAction(a), "invalid action=" + a);
    }

    @Override
    public String environmentName() {
        return EnvironmentTreasure.NAME;
    }

    @Override
    public Pair<Integer, Integer> getPosXMinMax() {
        return par.posXMinMax();
    }

    @Override
    public Pair<Integer, Integer> getPosYMinMax() {
        return par.posYMinMax();
    }

    @Override
    public List<ActionGrid> getValidActions() {
        return par.validActions();
    }

    @Override
    public boolean isTerminalNonFail(StateGrid state) {
        return par.terminalNonFailsStates().contains(state);
    }

    @Override
    public boolean isFail(StateGrid state) {
        return par.failStates().contains(state);
    }

    @Override
    public boolean isWall(StateGrid state) {
        return par.wallStates().contains(state);
    }

    @Override
    public Double rewardAtTerminalPos(StateGrid state) {
        validateTerminalState(state);
        return (isFail(state)) ? par.rewardAtFailPos() : par.rewardAtGoalPos().apply(state);
    }

    @Override
    public Double rewardMove() {
        return par.rewardMove();
    }


    @Override
    public boolean isValidState(StateGrid state) {
        return state.x() >= par.posXMinMax().getFirst() && state.x() <= par.posXMinMax().getSecond() &&
                state.y() >= par.posYMinMax().getFirst() && state.y() <= par.posYMinMax().getSecond();
    }

    @Override
    public boolean isValidAction(ActionGrid action) {
        return par.validActions().contains(action);
    }

    public PosXyInt xyMin() {
        return PosXyInt.of(par.posXMinMax().getFirst(), par.posYMinMax().getFirst());
    }

    public PosXyInt xyMax() {
        return PosXyInt.of(par.posXMinMax().getSecond(), par.posYMinMax().getSecond());
    }

}
