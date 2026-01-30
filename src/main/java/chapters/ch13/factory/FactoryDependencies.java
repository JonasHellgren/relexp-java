package chapters.ch13.factory;

import chapters.ch13.domain.searcher.searcher.Dependencies;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.EnvironmentJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.EnvironmentLane;
import chapters.ch13.implem.lane_change.StateLane;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryDependencies {


    public static Dependencies<StateJumper, ActionJumper> climberTest() {
        var searcherSettings = FactorySearcherSettings.forTestClimber();
        var environment = EnvironmentJumper.create();
        var nameFunction = FactoryNameFunction.rand3DigitClimber;
        var rolloutPolicy = FactoryRolloutPolicy.rolloutPolicyClimb;
        return Dependencies.<StateJumper, ActionJumper>builder()
                .searcherSettings(searcherSettings)
                .environment(environment)
                .nameFunction(nameFunction)
                .rolloutPolicy(rolloutPolicy)
                .build();
    }

    public static Dependencies<StateJumper, ActionJumper> climberRunner(
            int maxIterations,
            FactorySearcherSettings.RunnerSettings runnerSettings) {
        var searcherSettings = FactorySearcherSettings.forRunnerClimber(maxIterations, runnerSettings);
        var environment = EnvironmentJumper.create();
        var nameFunction = FactoryNameFunction.rand3DigitClimber;
        var rolloutPolicy = FactoryRolloutPolicy.rolloutPolicyClimb;
        return Dependencies.<StateJumper, ActionJumper>builder()
                .searcherSettings(searcherSettings)
                .environment(environment)
                .nameFunction(nameFunction)
                .rolloutPolicy(rolloutPolicy)
                .build();
    }


    public static Dependencies<StateLane, ActionLane> laneTest() {
        var searcherSettings = FactorySearcherSettings.forTestLane();
        var environment = EnvironmentLane.create();
        var nameFunction = FactoryNameFunction.rand5DigitLane;
        var rolloutPolicy = FactoryRolloutPolicy.rolloutPolicyLaneZeroOrRandom;
        return Dependencies.<StateLane, ActionLane>builder()
                .searcherSettings(searcherSettings)
                .environment(environment)
                .nameFunction(nameFunction)
                .rolloutPolicy(rolloutPolicy)
                .build();
    }



}
