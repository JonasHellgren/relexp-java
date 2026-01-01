package chapters.ch5._shared.evaluation;

import chapters.ch5._shared.episode_generator.EpisodeGeneratorI;
import chapters.ch5.domain.environment.ExperienceMc;
import chapters.ch5.domain.environment.StartStateSupplierI;
import chapters.ch5.domain.memory.StateMemoryMcI;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.math.LogarithmicDecay;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.math3.util.Pair;
import java.util.ArrayList;
import java.util.List;

import static chapters.ch5._shared.helper.ExperiencesInfo.isFirstVisit;

/**
 * This class represents a policy evaluation algorithm using Monte Carlo methods.
 * It updates the value function of a policy based on experiences gathered from an environment.
 */

@Builder
@NonNull
@Setter
public class StatePolicyEvaluationMc {
    public static final double LEARNING_RATE = 0.001;
    public static final Settings DEFAULT_SETTINGS = Settings.of(Pair.create(LEARNING_RATE, LEARNING_RATE), 1, 10_000);

    StartStateSupplierI startStateSupplier;
    EpisodeGeneratorI episodeGenerator;
    @Getter
    StateMemoryMcI memory;
    LogarithmicDecay learningRate;
    Settings settings;
    @Getter
    @Builder.Default
    List<Double> errorList = new ArrayList<>();

    /**
     * Evaluates the policy using Monte Carlo methods.
     * This method iterates over episodes, updates the value function based on experiences,
     * and repeats this process for a specified number of iterations.
     */
    public void evaluate() {
        var counter = Counter.ofMaxCount(settings.nIterations());
        errorList.clear();
        while (counter.isNotExceeded()) {
            var stateStart = startStateSupplier.getStartState();
            var experiences = episodeGenerator.generate(stateStart);
            updateMemoryFromExperiences(experiences, counter.getCount());
            counter.increase();
        }
    }

    private void updateMemoryFromExperiences(List<ExperienceMc> experiences, int iIter) {
        int nExperiences = experiences.size();
        double sumRewards = 0;
        for (int i = nExperiences - 1; i >= 0; i--) {
            var exp = experiences.get(i);
            sumRewards = settings.gamma() * sumRewards + exp.stepReturn().reward();
            var state = exp.state();
            boolean firstVisit = isFirstVisit(exp.state(), i, experiences);
            if (firstVisit) {
                double value = memory.read(state);
                double lr = learningRate.calcOut(iIter);
                memory.write(state, value + lr * (sumRewards - value));
                errorList.add(Math.abs(sumRewards - value));
            }
        }
    }

}
