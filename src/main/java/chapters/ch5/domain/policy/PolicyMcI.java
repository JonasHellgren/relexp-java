package chapters.ch5.domain.policy;


import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.StateMcI;

public interface PolicyMcI {
    ActionMcI chooseAction(StateMcI state);
}
