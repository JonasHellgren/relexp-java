package chapters.ch4.implem.blocked_road_lane.core;

import chapters.ch4.domain.param.InformerGridParamsI;
import core.foundation.gadget.normal_distribution.NormalSampler;
import core.foundation.gadget.pos.PosXyInt;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InformerRoadParams implements InformerGridParamsI {

    EnvironmentParametersRoad par;

    public static InformerRoadParams create(EnvironmentParametersRoad par) {
        return new InformerRoadParams(par);
    }

    @Override
    public String environmentName() {
        return EnvironmentRoad.NAME;
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
        return false;
    }

    @Override
    public Double rewardAtTerminalPos(StateGrid state) {
        return (isFail(state)) ? sampleFailReward() : 0.0;
    }

    private double sampleFailReward() {
        var sampler = NormalSampler.of(par.rewardAtFailPos().mean(), par.rewardAtFailPos().std());
        return sampler.generateSample();
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
