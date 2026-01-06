package chapters.ch4.implem.cliff_walk.core;

import chapters.ch4.domain.param.InformerGridParamsI;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentParametersRoad;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import com.google.common.base.Preconditions;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InformerCliff implements InformerGridParamsI {

    EnvironmentParametersCliff par;

    public static InformerCliff create(EnvironmentParametersCliff parameters) {
        return new InformerCliff(parameters);
    }


    @Override
    public void validateStateAndAction(StateGrid s, ActionGrid a) {
        Preconditions.checkArgument(isValidState(s),"invalid state="+ s);
        Preconditions.checkArgument(isValidAction(a),"invalid action="+ a);
    }

    @Override
    public String environmentName() {
        return EnvironmentRoad.NAME;
    }

    @Override
    public Pair<Integer, Integer> getPosXMinMax() {
        return par.getPosXMinMax();
    }

    @Override
    public Pair<Integer, Integer> getPosYMinMax() {
        return par.getPosYMinMax();
    }

    @Override
    public List<ActionGrid> getValidActions() {
        return par.getValidActions();
    }

    @Override
    public boolean isTerminalNonFail(StateGrid state) {
        return par.getTerminalNonFailsStates().contains(state);
    }

    @Override
    public boolean isFail(StateGrid state) {
        return par.getFailStates().contains(state);
    }

    @Override
    public boolean isWall(StateGrid state) {
        return false;
    }

    @Override
    public Double rewardAtTerminalPos(StateGrid state) {
        validateTerminalState(state);
        return (isFail(state)) ? par.getRewardAtFailPos() : par.getRewardAtGoalPos();
    }

    @Override
    public Double rewardMove() {
        return par.rewardMove();
    }


    @Override
    public boolean isValidState(StateGrid state) {
        return state.x() >= par.getPosXMinMax().getFirst() && state.x() <= par.getPosXMinMax().getSecond() &&
                state.y() >= par.getPosYMinMax().getFirst() && state.y() <= par.getPosYMinMax().getSecond();
    }

    @Override
    public boolean isValidAction(ActionGrid action) {
        return par.getValidActions().contains(action);
    }



}
