package chapters.ch5.implem.splitting;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.policies.SplittingPathPolicyOptimal;
import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.policy.PolicyMcI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/***
 * Wraps policy used in a_concepts
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PolicyMcSplittingOptimal implements PolicyMcI {

    SplittingPathPolicyOptimal policy;

    public static PolicyMcSplittingOptimal create(EnvironmentParametersSplitting parameters) {
        return new PolicyMcSplittingOptimal(SplittingPathPolicyOptimal.of(parameters));
    }

    @Override
    public ActionMcI chooseAction(StateMcI state) {
        var action = policy.chooseAction(SplittingPathAdapter.getStateGrid((StateSplittingMc) state));
        return ActionSplittingMc.of(action);
    }
}
