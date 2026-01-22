package chapters.ch5.domain.policy_evaluator;

import chapters.ch5.domain.environment.ExperienceMc;
import core.foundation.gadget.cond.Counter;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static chapters.ch5.domain.policy_evaluator.ExperiencesInfo.isFirstVisit;


/**
 * Evaluates a policy using Monte Carlo methods.
 * This class iterates over episodes, updates the value function based on experiences,
 * and repeats this process.
 */
@Builder
@Getter
public class StateActionPolicyEvaluationMc {

    EvaluatorDependencies dependencies;

    public static StateActionPolicyEvaluationMc of(EvaluatorDependencies dependencies) {
        return new StateActionPolicyEvaluationMc(dependencies);
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

    private void updateMemoryFromExperiences(List<ExperienceMc> experiences, int nIter) {
        var d=dependencies;
        int nExperiences = experiences.size();
        double sumRewards = 0;
        for (int i = nExperiences - 1; i >= 0; i--) {
            var exp = experiences.get(i);
            sumRewards = d.settings().gamma() * sumRewards + exp.stepReturn().reward();
            boolean firstVisit = isFirstVisit(exp.state(), i, experiences);
            if (firstVisit) {
                double value = d.stateActionMemory().read(exp.state(), exp.action());
                var lr=d.learningRate().calcOut(nIter);
                //System.out.println("exp.state() = " + exp.state() + ", exp.action() = " + exp.action() + ", value = " + value + ", rewardAccumulator = " + rewardAccumulator);
                d.stateActionMemory().write(exp.state(), exp.action(), value + lr * (sumRewards - value));
                d.errorList().add(Math.abs(sumRewards - value));
            }
        }
    }


}
