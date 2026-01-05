package chapters.ch3.factory;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Factory class for creating splitting path problem environment parameters.
 */
@UtilityClass
public class EnvironmentParametersSplittingFactory {

    static final double REWARD_COIN = 1d;
    static final double REWARD_NO_COIN = 0d;
    static final double REWARD_FAIL = 0d;
    static final double REWARD_MOVE = 0d;

    public static EnvironmentParametersSplitting produce() {

        return EnvironmentParametersSplitting.builder()
                .posXMinMax(Pair.create(0, 5))
                .posYMinMax(Pair.create(0, 2))
                //.validActions(ActionGrid.getAllActions())
                .validActions(List.of(ActionGrid.N, ActionGrid.E, ActionGrid.S))
                .terminalNonFailsStates(getGoalStates())
                .failStates(getFailStates())
                .wallStates(getWallStates())
                .splitState(Set.of(StateGrid.of(2, 1)))
                .statesExceptSplit(Set.of(StateGrid.of(0,1),StateGrid.of(1,1),
                        StateGrid.of(2,2),StateGrid.of(3,2),StateGrid.of(4,2),
                        StateGrid.of(2,0),StateGrid.of(3,0),StateGrid.of(4,0)))
                .rewardAtFailPos(REWARD_FAIL)
                .rewardAtGoalPos(s -> s.equals(StateGrid.of(5, 2))
                        ? REWARD_COIN
                        : REWARD_NO_COIN)
                .rewardMove(REWARD_MOVE)
                .build();
    }

    private static Set<StateGrid> getWallStates() {
        Set<StateGrid> walls = new HashSet<>();
        walls.addAll(getStatesMinMaxXatY(0, 1, 2));
        walls.addAll(getStatesMinMaxXatY(3, 5, 1));
        walls.addAll(getStatesMinMaxXatY(0, 1, 0));
        return walls;
    }

    private static Set<StateGrid> getFailStates() {
        return new HashSet<>();
    }


    private static Set<StateGrid> getGoalStates() {
        return Set.of(StateGrid.of(5, 2), StateGrid.of(5, 0));
    }

    private static Set<StateGrid> getStatesMinMaxXatY(int minX, int maxX, int y) {
        return IntStream.rangeClosed(minX, maxX)
                .mapToObj(x -> StateGrid.of(x, y))
                .collect(Collectors.toSet());
    }

}
