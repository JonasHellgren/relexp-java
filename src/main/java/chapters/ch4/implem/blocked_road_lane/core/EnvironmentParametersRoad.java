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
 */
@Builder
@With
public record EnvironmentParametersRoad(
     Pair<Integer, Integer> posXMinMax,
     Pair<Integer, Integer> posYMinMax,
     List<ActionGrid> validActions,
     Set<StateGrid> terminalNonFailsStates,
     Set<StateGrid> failStates,
     MeanAndStd rewardAtFailPos,
     Double rewardMove)
{
}
