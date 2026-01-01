package chapters.ch5.implem.splitting;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.implem.splitting_path_problem.SplittingPathPolicyRandom;
import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.policy.PolicyMcI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PolicyMcSplittingRandom implements PolicyMcI {

    SplittingPathPolicyRandom policy;

    public static PolicyMcSplittingRandom create(EnvironmentParametersSplitting parameters) {
        return new PolicyMcSplittingRandom(SplittingPathPolicyRandom.of(parameters));
    }

    @Override
    public ActionMcI chooseAction(StateMcI state) {
        var action = policy.chooseAction(SplittingPathAdapter.getStateGrid((StateSplittingMc) state));
        return ActionSplittingMc.of(action);
    }

}
