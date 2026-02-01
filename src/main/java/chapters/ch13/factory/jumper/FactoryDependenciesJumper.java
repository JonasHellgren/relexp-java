package chapters.ch13.factory.jumper;

import chapters.ch13.domain.searcher.core.Dependencies;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.EnvironmentJumper;
import chapters.ch13.implem.jumper.StateJumper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryDependenciesJumper {


    public static Dependencies<StateJumper, ActionJumper> test() {
        var searcherSettings = FactorySearcherParametersJumper.test();
        var environment = EnvironmentJumper.of(JumperParametersFactory.produce());
        var nameFunction = FactoryNameFunctionJumper.rand3DigitClimber;
        var rolloutPolicy = FactoryRolloutPolicyJumper.rolloutPolicy;
        return Dependencies.<StateJumper, ActionJumper>builder()
                .searcherSettings(searcherSettings)
                .environment(environment)
                .nameFunction(nameFunction)
                .rolloutPolicy(rolloutPolicy)
                .build();
    }

    public static Dependencies<StateJumper, ActionJumper> runner(
            int maxIterations,
            RunnerSettings runnerSettings) {
        var searcherSettings = FactorySearcherParametersJumper.runner(maxIterations, runnerSettings);
        var environment = EnvironmentJumper.of(JumperParametersFactory.produce());
        var nameFunction = FactoryNameFunctionJumper.rand3DigitClimber;
        var rolloutPolicy = FactoryRolloutPolicyJumper.rolloutPolicy;

        return Dependencies.<StateJumper, ActionJumper>builder()
                .searcherSettings(searcherSettings)
                .environment(environment)
                .nameFunction(nameFunction)
                .rolloutPolicy(rolloutPolicy)
                .build();
    }

}
