package chapters.ch3.implem.splitting_path_problem;

import chapters.ch3.policies.SplittingPathPolicyI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Implements value iteration algorithm
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PolicyEvaluatorSplittingPath {

    EvaluatorDependencies dependencies;

    public static PolicyEvaluatorSplittingPath of(EvaluatorDependencies dependencies) {
        return new PolicyEvaluatorSplittingPath(dependencies);
    }

    public void evaluate(SplittingPathPolicyI policy) {
        var d=dependencies;
        d.errorList().clear();
        for (int i = 0; i < d.nFits(); i++) {
            var state=d.startStateSupplier().getStartState();
            var action = policy.chooseAction(state);
            var sr = d.environment().step(state, action);
            var stateNext = sr.sNext();
            double reward = sr.reward();
            double valueNext = d.memory().read(stateNext);
            double valueTar = d.discountFactor() * valueNext + reward;
            double lr=d.learningRate().calcOut(i);
            d.memory().fit(state, valueTar, lr);
            d.errorList().add(Math.abs(valueTar - d.memory().read(state)));
        }
    }

}
