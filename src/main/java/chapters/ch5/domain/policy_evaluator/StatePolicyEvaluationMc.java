package chapters.ch5.domain.policy_evaluator;

import chapters.ch5.domain.episode_generator.EpisodeGeneratorI;
import chapters.ch5.domain.environment.ExperienceMc;
import chapters.ch5.domain.environment.StartStateSupplierI;
import chapters.ch5.domain.memory.StateMemoryMcI;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.math.LogarithmicDecay;
import lombok.*;
import org.apache.commons.math3.util.Pair;
import java.util.ArrayList;
import java.util.List;

import static chapters.ch5.domain.policy_evaluator.ExperiencesInfo.isFirstVisit;

/**
 * This class represents a policy evaluation algorithm using Monte Carlo methods.
 * It updates the value function of a policy based on experiences gathered from an environment.
 */

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatePolicyEvaluationMc {
    public static final double LEARNING_RATE = 0.001;
    public static final EvaluatorSettings DEFAULT_SETTINGS =
            EvaluatorSettings.of(Pair.create(LEARNING_RATE, LEARNING_RATE), 1, 10_000);

/*    StartStateSupplierI startStateSupplier;
    EpisodeGeneratorI episodeGenerator;
    @Getter
    StateMemoryMcI memory;
    LogarithmicDecay learningRate;
    EvaluatorSettings settings;
    @Getter
    @Builder.Default
    List<Double> errorList = new ArrayList<>();*/

    EvaluatorDependencies dependencies;

    public static StatePolicyEvaluationMc of(EvaluatorDependencies dependencies) {
        return new StatePolicyEvaluationMc(dependencies);
    }

    /**
     * Evaluates the policy using Monte Carlo methods.
     * This method iterates over episodes, updates the value function based on experiences,
     * and repeats this process for a specified number of iterations.
     */
    public void evaluate() {
        var d=dependencies;
        var counter = Counter.ofMaxCount(d.settings().nIterations());
        d.errorList().clear();
        while (counter.isNotExceeded()) {
            var stateStart = d.startStateSupplier().getStartState();
            var experiences = d.episodeGenerator().generate(stateStart);
            updateMemoryFromExperiences(experiences, counter.getCount());
            counter.increase();
        }
    }

    private void updateMemoryFromExperiences(List<ExperienceMc> experiences, int iIter) {
        var d=dependencies;
        int nExperiences = experiences.size();
        double sumRewards = 0;
        for (int i = nExperiences - 1; i >= 0; i--) {
            var exp = experiences.get(i);
            sumRewards = d.settings().gamma() * sumRewards + exp.stepReturn().reward();
            var state = exp.state();
            boolean firstVisit = isFirstVisit(exp.state(), i, experiences);
            if (firstVisit) {
                double value = d.stateMemory().read(state);
                double lr = d.learningRate().calcOut(iIter);
                d.stateMemory().write(state, value + lr * (sumRewards - value));
                d.errorList().add(Math.abs(sumRewards - value));
            }
        }
    }

}
