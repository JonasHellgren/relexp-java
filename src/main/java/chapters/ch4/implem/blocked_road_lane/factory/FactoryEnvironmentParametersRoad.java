package chapters.ch4.implem.blocked_road_lane.factory;

import chapters.ch4.implem.blocked_road_lane.core.EnvironmentParametersRoad;
import core.foundation.gadget.math.MeanAndStd;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

import java.util.List;
import java.util.Set;

/**
 * Factory class for creating EnvironmentParametersRoad instances.
 * This class provides methods for creating environment parameters with specific settings.
 */
@UtilityClass
public class FactoryEnvironmentParametersRoad {

    public static EnvironmentParametersRoad produceRoadFixedFailReward() {
        return EnvironmentParametersRoad.builder()
                .posXMinMax(Pair.create(0, 3))
                .posYMinMax(Pair.create(0, 1))
                .validActions(List.of(ActionGrid.N, ActionGrid.E, ActionGrid.S))
                .terminalNonFailsStates(Set.of(StateGrid.of(3, 0)))
                .failStates(Set.of(StateGrid.of(3, 1)))
                .rewardAtFailPos(MeanAndStd.of(-100, 0d))
                .rewardMove(-1d)
                .build();
    }

    public static EnvironmentParametersRoad produceRoadRandomReward() {
        return produceRoadFixedFailReward()
                .withRewardAtFailPos(MeanAndStd.of(-100, 30d));
    }
}
