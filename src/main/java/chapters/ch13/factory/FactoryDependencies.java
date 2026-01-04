package chapters.ch13.factory;

import k_mcts.domain.searcher.searcher.Dependencies;
import k_mcts.environments.jumper.ActionJumper;
import k_mcts.environments.jumper.EnvironmentJumper;
import k_mcts.environments.jumper.StateJumper;
import k_mcts.environments.lane_change.ActionLane;
import k_mcts.environments.lane_change.EnvironmentLane;
import k_mcts.environments.lane_change.StateLane;
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
