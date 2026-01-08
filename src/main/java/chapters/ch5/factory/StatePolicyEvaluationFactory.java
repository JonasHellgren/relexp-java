package chapters.ch5.factory;

import chapters.ch5.domain.episode_generator.EpisodeGenerator;
import chapters.ch5.domain.policy_evaluator.EvaluatorDependencies;
import chapters.ch5.domain.policy_evaluator.EvaluatorSettings;
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

import java.util.ArrayList;

@UtilityClass
public class StatePolicyEvaluationFactory {

    public static StatePolicyEvaluationMc createWalk(EvaluatorSettings settings) {
        var parameters = GridParametersWalkFactory.produce();
        var policy = PolicyMcWalk.create();
        var env= EnvironmentWalk.of(parameters);
        var dep= EvaluatorDependencies.builder()
                .episodeGenerator(EpisodeGenerator.of(env,policy))
                .startStateSupplier(StartStateSupplierWalk.create())
                .stateMemory(MemoryWalk.create())
                .learningRate(settings.getDecayingLearningRate())
                .settings(settings)
                .errorList(new ArrayList<>())
                .build();
        return StatePolicyEvaluationMc.of(dep);

        /*return StatePolicyEvaluationMc.builder()
                .episodeGenerator(EpisodeGenerator.of(env,policy))
                .startStateSupplier(StartStateSupplierWalk.create())
                .memory(MemoryWalk.create())
                .learningRate(settings.getDecayingLearningRate())
                .settings(settings)
                .build();*/
    }

    public static StatePolicyEvaluationMc createSplittingOptimalPolicy(EvaluatorSettings settings) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var policy = PolicyMcSplittingOptimal.create(parameters);
        return getPolicyEvaluationMc(settings, parameters, policy);
    }

    public static StatePolicyEvaluationMc createSplittingRandomPolicy(EvaluatorSettings settings) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var policy = PolicyMcSplittingRandom.create(parameters);
        return getPolicyEvaluationMc(settings, parameters, policy);
    }

    private static StatePolicyEvaluationMc getPolicyEvaluationMc(EvaluatorSettings settings,
                                                                 EnvironmentParametersSplitting parameters,
                                                                 PolicyMcI policy) {
        var environment = EnvironmentSplittingMc.of(parameters);
        var dep= EvaluatorDependencies.builder()
                .episodeGenerator(EpisodeGenerator.of(environment,policy))
                .startStateSupplier(StartStateSupplierMostLeftSplitting.create())
                .stateMemory(StateMemorySplitting.create())
                .learningRate(settings.getDecayingLearningRate())
                .settings(settings)
                .errorList(new ArrayList<>())
                .build();
        return StatePolicyEvaluationMc.of(dep);



/*        return StatePolicyEvaluationMc.builder()
                .episodeGenerator(EpisodeGenerator.of(environment, policy))
                .startStateSupplier(StartStateSupplierMostLeftSplitting.create())
                .memory(StateMemorySplitting.create())
                .learningRate(settings.getDecayingLearningRate())
                .settings(settings)
                .build();*/
    }

}
