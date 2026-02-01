package chapters.ch13.factory.lane_change;

import chapters.ch13.domain.searcher.core.Dependencies;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.EnvironmentLane;
import chapters.ch13.implem.lane_change.StateLane;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryDependenciesLaneChange {

    public static Dependencies<StateLane, ActionLane> laneTest() {
        var searcherSettings = FactorySearcherSettingsLaneChange.test();
        var environment = EnvironmentLane.create(LaneChangeParametersFactory.produce());
        var nameFunction = FactoryNameFunctionLaneChange.rand5DigitLane;
        var rolloutPolicy = FactoryRolloutPolicyLaneChange.zeroOrRandom;
        return Dependencies.<StateLane, ActionLane>builder()
                .searcherSettings(searcherSettings)
                .environment(environment)
                .nameFunction(nameFunction)
                .rolloutPolicy(rolloutPolicy)
                .build();
    }


    public static Dependencies<StateLane, ActionLane> runner() {
        return laneTest();
    }
}
