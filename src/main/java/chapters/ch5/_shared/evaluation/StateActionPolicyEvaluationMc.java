package chapters.ch5._shared.evaluation;

import chapters.ch5._shared.episode_generator.EpisodeGeneratorI;
import chapters.ch5.domain.environment.ExperienceMc;
import chapters.ch5.domain.environment.StartStateSupplierI;
import chapters.ch5.domain.memory.StateActionMemoryMcI;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.math.LogarithmicDecay;
import lombok.Builder;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

import static chapters.ch5._shared.helper.ExperiencesInfo.isFirstVisit;


/**
 * Evaluates a policy using Monte Carlo methods.
 * This class iterates over episodes, updates the value function based on experiences,
 * and repeats this process.
 */
@Builder
public class StateActionPolicyEvaluationMc {

    StartStateSupplierI startStateSupplier;
    EpisodeGeneratorI episodeGenerator;
    @Getter
    StateActionMemoryMcI memory;
    Settings settings;
    LogarithmicDecay learningRate;
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

    private void updateMemoryFromExperiences(List<ExperienceMc> experiences, int nIter) {
        int nExperiences = experiences.size();
        double sumRewards = 0;
        for (int i = nExperiences - 1; i >= 0; i--) {
            var exp = experiences.get(i);
            sumRewards = settings.gamma() * sumRewards + exp.stepReturn().reward();
            boolean firstVisit = isFirstVisit(exp.state(), i, experiences);
            if (firstVisit) {
                double value = memory.read(exp.state(), exp.action());
                var lr=learningRate.calcOut(nIter);
                //System.out.println("exp.state() = " + exp.state() + ", exp.action() = " + exp.action() + ", value = " + value + ", rewardAccumulator = " + rewardAccumulator);
                memory.write(exp.state(), exp.action(), value + lr * (sumRewards - value));
                errorList.add(Math.abs(sumRewards - value));
            }
        }
    }


}
