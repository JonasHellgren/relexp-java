package chapters.ch4.implem.cliff_walk.core;

import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import com.google.common.base.Preconditions;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.math3.util.Pair;
import java.util.List;
import java.util.Set;

/**
 * Represents the parameters for the Cliff Walk environment.
 * This class implements the GridParametersI interface and provides the necessary
 * methods for validating states and actions, determining terminal and fail states,
 * and calculating rewards.
 */
@Builder
public record EnvironmentParametersCliff (
     Pair<Integer, Integer> posXMinMax,
     Pair<Integer, Integer> posYMinMax,
     List<ActionGrid> validActions,
     Set<StateGrid> terminalNonFailsStates,
     Set<StateGrid> failStates,
     Double rewardAtFailPos,
     Double rewardAtGoalPos,
     Double rewardMove)

{}


