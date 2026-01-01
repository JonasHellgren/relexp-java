package chapters.ch4.implem.blocked_road_lane.core;

import core.foundation.gadget.math.MeanAndStd;
import core.foundation.gadget.normal_distribution.NormalSampler;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.apache.commons.math3.util.Pair;
import java.util.List;
import java.util.Set;

/**
 * This class represents the parameters for a road environment in the BlockedRoadLane domain.
 * It extends the GridParametersI interface, which is used to define the parameters for a grid environment.
 * Actual values are defined in the AgentGridParametersFactoryRoad class.
 */
@Getter
@Builder
@With
public class EnvironmentParametersRoad implements EnvironmentGridParametersI {

    private Pair<Integer, Integer> posXMinMax;
    private Pair<Integer, Integer> posYMinMax;
    private List<ActionGrid> validActions;
    private Set<StateGrid> terminalNonFailsStates;
    private Set<StateGrid> failStates;
    private MeanAndStd rewardAtFailPos;
    private Double rewardMove;

    @Override
    public String environmentName() {
        return EnvironmentRoad.NAME;
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
        return false;
    }

    @Override
    public Double rewardAtTerminalPos(StateGrid state) {
        return (isFail(state)) ? sampleFailReward() : 0.0;
    }

    private double sampleFailReward() {
        var sampler = NormalSampler.of(rewardAtFailPos.mean(), rewardAtFailPos.std());
        return sampler.generateSample();
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
