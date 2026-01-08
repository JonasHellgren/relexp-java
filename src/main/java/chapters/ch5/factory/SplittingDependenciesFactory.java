package chapters.ch5.factory;

import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch5.domain.episode_generator.EpisodeGenerator;
import chapters.ch5.domain.policy.PolicyMcI;
import chapters.ch5.domain.policy_evaluator.EvaluatorDependencies;
import chapters.ch5.domain.policy_evaluator.EvaluatorSettings;
import chapters.ch5.implem.splitting.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;

@UtilityClass
public class SplittingDependenciesFactory {

    public static final double LEARNING_RATE = 0.001;
    public static final EvaluatorSettings SETTINGS =
            EvaluatorSettings.of(Pair.create(LEARNING_RATE, LEARNING_RATE), 1, 10_000);


    public static EvaluatorDependencies optimalPolicy(EvaluatorSettings settings) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var policy = PolicyMcSplittingOptimal.create(parameters);
        return getPolicyEvaluationMc(settings, parameters, policy);
    }

    public static EvaluatorDependencies createSplittingRandomPolicy(EvaluatorSettings settings) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var policy = PolicyMcSplittingRandom.create(parameters);
        return getPolicyEvaluationMc(settings, parameters, policy);
    }

    private static EvaluatorDependencies getPolicyEvaluationMc(EvaluatorSettings settings,
                                                               EnvironmentParametersSplitting parameters,
                                                               PolicyMcI policy) {
        var environment = EnvironmentSplittingMc.of(parameters);
        var dep = EvaluatorDependencies.builder()
                .episodeGenerator(EpisodeGenerator.of(environment, policy))
                .startStateSupplier(StartStateSupplierMostLeftSplitting.create())
                .stateMemory(StateMemorySplitting.create())
                .learningRate(settings.getDecayingLearningRate())
                .settings(settings)
                .errorList(new ArrayList<>())
                .build();
        return dep;

    }

}
