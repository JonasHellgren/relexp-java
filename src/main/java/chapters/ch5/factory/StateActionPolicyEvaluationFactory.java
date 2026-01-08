package chapters.ch5.factory;

import chapters.ch5.domain.episode_generator.EpisodeGenerator;
import chapters.ch5.domain.policy_evaluator.Settings;
import chapters.ch5.domain.policy_evaluator.StateActionPolicyEvaluationMc;
import chapters.ch5.implem.dice.EnvironmentDice;
import chapters.ch5.implem.dice.PolicyDice;
import chapters.ch5.implem.dice.StartStateSupplierDice;
import chapters.ch5.implem.dice.StateActionMemoryDice;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

/**
 * Factory class for creating instances of StateActionPolicyEvaluationMc.
 */
@UtilityClass
public class StateActionPolicyEvaluationFactory {

    public static final double LR_START = 0.1;  //0.1
    public static final double LR_END = 0.01;  //0.001
    public static final double GAMMA = 1.0;
    public static final int N_ITERATIONS = 10_000; //10_000;
    public static final double PROB_RANDOM_ACTION = 1.0;

    public static StateActionPolicyEvaluationMc createDice() {
        var env= EnvironmentDice.create();
        var settings= Settings.ofRandomActionAlso(
                Pair.create(LR_START, LR_END), //initial and final values for the decaying learning rate
                GAMMA,
                N_ITERATIONS,
                PROB_RANDOM_ACTION);
        var startStateSupplier = StartStateSupplierDice.create();
        var memory = StateActionMemoryDice.create();
        var policy = PolicyDice.of(memory,settings);
        var episodeGenerator= EpisodeGenerator.of(env, policy);
        return StateActionPolicyEvaluationMc.builder()
                .startStateSupplier(startStateSupplier)
                .episodeGenerator(episodeGenerator)
                .memory(memory)
                .learningRate(settings.getDecayingLearningRate())
                .settings(settings)
                .build();
    }

}
