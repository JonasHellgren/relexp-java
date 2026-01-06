package chapters.ch3.implem.splitting_path_problem;

import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.Builder;
import lombok.With;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Builder
@With
public record EnvironmentParametersSplitting (

     Pair<Integer, Integer> posXMinMax,
     Pair<Integer, Integer> posYMinMax,
     List<ActionGrid> validActions,
     Set<StateGrid> terminalNonFailsStates,
     Set<StateGrid> failStates,
     Set<StateGrid> wallStates,
     Set<StateGrid> splitState,
     Set<StateGrid> statesExceptSplit,
     Function<StateGrid, Double> rewardAtGoalPos,
     Double rewardAtFailPos,
     Double rewardMove)

{}

