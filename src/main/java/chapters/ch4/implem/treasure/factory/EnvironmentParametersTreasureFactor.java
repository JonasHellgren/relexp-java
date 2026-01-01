package chapters.ch4.implem.treasure.factory;

import chapters.ch4.implem.treasure.core.EnvironmentParametersTreasure;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Factory class for creating EnvironmentParametersTreasure instances.
 * This class provides a method for creating environment parameters with specific settings for the Treasure Hunt environment.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentParametersTreasureFactor {

    static final double REWARD_SMALL_TREASURE = 10d;
    static final double REWARD_BIG_TREASURE = 100d;
    static final double REWARD_FAIL = -100d;
    static final double REWARD_MOVE = -1d;

    public static EnvironmentParametersTreasure produce() {

        return EnvironmentParametersTreasure.builder()
                .posXMinMax(Pair.create(0, 9))
                .posYMinMax(Pair.create(0, 3))
                .validActions(ActionGrid.getAllActions())
                //.validActions(List.of(ActionGrid.N, ActionGrid.S, ActionGrid.E))
                .terminalNonFailsStates(getGoalStates())
                .failStates(getFailStates())
                .wallStates(getWallStates())
                .rewardAtFailPos(REWARD_FAIL)
                .rewardAtGoalPos(s -> s.equals(StateGrid.of(4, 0))
                        ? REWARD_SMALL_TREASURE
                        : REWARD_BIG_TREASURE)
                .rewardMove(REWARD_MOVE)
                .build();
    }

    private static Set<StateGrid> getWallStates() {
        Set<StateGrid> walls = new HashSet<>();
        walls.addAll(getStatesMinMaxXatY(0, 3, 3));
        walls.addAll(getStatesMinMaxXatY(8, 9, 3));
        walls.add(StateGrid.of(0, 2));
        walls.addAll(getStatesMinMaxXatY(6, 9, 2));
        walls.add(StateGrid.of(4, 1));
        walls.add(StateGrid.of(0, 0));
        walls.add(StateGrid.of(9, 0));
        return walls;
    }

    private static Set<StateGrid> getFailStates() {
        Set<StateGrid> fails = new HashSet<>();
        fails.add(StateGrid.of(7, 3));
        fails.add(StateGrid.of(1, 2));
        fails.add(StateGrid.of(1, 0));
        fails.addAll(getStatesMinMaxXatY(5, 8, 0));
        return fails;
    }


    private static Set<StateGrid> getGoalStates() {
        return Set.of(StateGrid.of(9, 1), StateGrid.of(4, 0));
    }

    private static Set<StateGrid> getStatesMinMaxXatY(int minX, int maxX, int y) {
        return IntStream.rangeClosed(minX, maxX)
                .mapToObj(x -> StateGrid.of(x, y))
                .collect(Collectors.toSet());
    }

}
