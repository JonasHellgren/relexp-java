package chapters.ch4.implem.treasure.core;

import com.google.common.base.Preconditions;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.function.Function;


/**
 * Represents the parameters for the Treasure Hunt environment.
 * This class provides the necessary info for validating states and actions,
 * determining terminal and fail states, and calculating rewards.
 */
@Builder
@With
public record EnvironmentParametersTreasure (
     Pair<Integer, Integer> posXMinMax,
     Pair<Integer, Integer> posYMinMax,
     List<ActionGrid> validActions,
     Set<StateGrid> terminalNonFailsStates,
     Set<StateGrid> failStates,
     Set<StateGrid> wallStates,
     Double rewardAtFailPos,
     Function<StateGrid, Double> rewardAtGoalPos,
     Double rewardMove)

{}

