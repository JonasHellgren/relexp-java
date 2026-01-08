package chapters.ch5.factory;

import chapters.ch5.domain.episode_generator.EpisodeGenerator;
import chapters.ch5.domain.policy_evaluator.EvaluatorDependencies;
import chapters.ch5.domain.policy_evaluator.EvaluatorSettings;
import chapters.ch5.implem.walk.EnvironmentWalk;
import chapters.ch5.implem.walk.MemoryWalk;
import chapters.ch5.implem.walk.PolicyMcWalk;
import chapters.ch5.implem.walk.StartStateSupplierWalk;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;

@UtilityClass
public class WalkDependenciesFactory {

    public static final double LEARNING_RATE = 0.001;
    public static final EvaluatorSettings SETTINGS =
            EvaluatorSettings.of(Pair.create(LEARNING_RATE, LEARNING_RATE), 1, 10_000);

    public static EvaluatorDependencies produce() {
        var parameters = GridParametersWalkFactory.produce();
        var policy = PolicyMcWalk.create();
        var env= EnvironmentWalk.of(parameters);
        var dep= EvaluatorDependencies.builder()
                .episodeGenerator(EpisodeGenerator.of(env,policy))
                .startStateSupplier(StartStateSupplierWalk.create())
                .stateMemory(MemoryWalk.create())
                .learningRate(SETTINGS.getDecayingLearningRate())
                .settings(SETTINGS)
                .errorList(new ArrayList<>())
                .build();
        return dep;
    }

}
