package chapters.ch13.factory.lane_change;

import chapters.ch13.domain.searcher.core.OuterDependencies;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.EnvironmentLane;
import chapters.ch13.implem.lane_change.StateLane;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryDependenciesLaneChange {

    public static OuterDependencies<StateLane, ActionLane> laneTest() {
        var searcherSettings = FactorySearcherSettingsLaneChange.test();
        var environment = EnvironmentLane.create(LaneChangeParametersFactory.produce());
        var nameFunction = FactoryNameFunctionLaneChange.rand5DigitLane;
        var rolloutPolicy = FactoryRolloutPolicyLaneChange.zeroOrRandom;
        return OuterDependencies.<StateLane, ActionLane>builder()
                .searcherSettings(searcherSettings)
                .environment(environment)
                .nameFunction(nameFunction)
                .rolloutPolicy(rolloutPolicy)
                .build();
    }


    public static OuterDependencies<StateLane, ActionLane> runner() {
        return laneTest();
    }
}
