package chapters.ch4.implem.cliff_walk.core;

import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.Builder;
import org.apache.commons.math3.util.Pair;
import java.util.List;
import java.util.Set;

/**
 * Represents the parameters for the Cliff Walk environment.
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


