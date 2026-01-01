package chapters.ch5.implem.walk;

import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.policy.PolicyMcI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PolicyMcWalk implements PolicyMcI {

    public static PolicyMcWalk create() {
        return new PolicyMcWalk();
    }

    @Override
    public ActionMcI chooseAction(StateMcI state) {
        return ActionWalk.random();
    }
}
