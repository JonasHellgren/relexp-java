package chapters.ch5.factory;

import chapters.ch5.domain.episode_generator.EpisodeGenerator;
import chapters.ch5.domain.policy_evaluator.Settings;
import chapters.ch5.domain.policy_evaluator.StatePolicyEvaluationMc;
import chapters.ch5.domain.policy.PolicyMcI;
import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import chapters.ch5.implem.splitting.*;
import chapters.ch5.implem.walk.EnvironmentWalk;
import chapters.ch5.implem.walk.MemoryWalk;
import chapters.ch5.implem.walk.PolicyMcWalk;
import chapters.ch5.implem.walk.StartStateSupplierWalk;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StatePolicyEvaluationFactory {

    public static StatePolicyEvaluationMc createWalk(Settings settings) {
        var parameters = GridParametersWalkFactory.produce();
        var policy = PolicyMcWalk.create();
        var env= EnvironmentWalk.of(parameters);
        return StatePolicyEvaluationMc.builder()
                .episodeGenerator(EpisodeGenerator.of(env,policy))
                .startStateSupplier(StartStateSupplierWalk.create())
                .memory(MemoryWalk.create())
                .learningRate(settings.getDecayingLearningRate())
                .settings(settings)
                .build();
    }

    public static StatePolicyEvaluationMc createSplittingOptimalPolicy(Settings settings) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var policy = PolicyMcSplittingOptimal.create(parameters);
        return getPolicyEvaluationMc(settings, parameters, policy);
    }

    public static StatePolicyEvaluationMc createSplittingRandomPolicy(Settings settings) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var policy = PolicyMcSplittingRandom.create(parameters);
        return getPolicyEvaluationMc(settings, parameters, policy);
    }

    private static StatePolicyEvaluationMc getPolicyEvaluationMc(Settings settings,
                                                                 EnvironmentParametersSplitting parameters,
                                                                 PolicyMcI policy) {
        var environment = EnvironmentSplittingMc.of(parameters);
        return StatePolicyEvaluationMc.builder()
                .episodeGenerator(EpisodeGenerator.of(environment, policy))
                .startStateSupplier(StartStateSupplierMostLeftSplitting.create())
                .memory(StateMemorySplitting.create())
                .learningRate(settings.getDecayingLearningRate())
                .settings(settings)
                .build();
    }

}
