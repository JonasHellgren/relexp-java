package chapters.ch4.implem.cliff_walk.factory;

import chapters.ch4.implem.cliff_walk.core.EnvironmentParametersCliff;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Factory class for creating EnvironmentParametersCliff instances.
 * This class provides a method for creating environment parameters with specific
 * settings for the Cliff Walk environment.
 */
@UtilityClass
public class FactoryEnvironmentParametersCliff {

    public static final int MIN_X_CLIFF = 0;
    public static final int MIN_Y_CLIFF = 0;
    public static final int MAX_X_CLIFF = 10;
    public static final int MAX_Y_CLIFF = 3;

    public static EnvironmentParametersCliff produceCliff() {

        Set<StateGrid> failStates = IntStream.rangeClosed(1, MAX_X_CLIFF-1)
                .mapToObj(x -> StateGrid.of(x, MIN_Y_CLIFF))
                .collect(Collectors.toSet());

        return EnvironmentParametersCliff.builder()
                .posXMinMax(Pair.create(MIN_X_CLIFF, MAX_X_CLIFF))
                .posYMinMax(Pair.create(MIN_Y_CLIFF, MAX_Y_CLIFF))
                .validActions(List.of(ActionGrid.N, ActionGrid.E, ActionGrid.S,ActionGrid.W))
                .terminalNonFailsStates(Set.of(StateGrid.of(MAX_X_CLIFF, MIN_Y_CLIFF)))
                .failStates(failStates)
                .rewardAtFailPos(-100d)
                .rewardAtGoalPos(10d)
                .rewardMove(-1d)
                .build();
    }


}
