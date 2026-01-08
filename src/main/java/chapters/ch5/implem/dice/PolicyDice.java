package chapters.ch5.implem.dice;

import chapters.ch5.domain.policy_evaluator.Settings;
import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.memory.StateActionMemoryMcI;
import chapters.ch5.domain.policy.PolicyMcI;
import core.foundation.util.rand.RandUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * This class represents a policy for a dice game environment.
 * It implements the PolicyMcI interface and provides a method for choosing an action based on the current state.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PolicyDice implements PolicyMcI {

    StateActionMemoryMcI memory;
    Settings settings;

    public static PolicyDice of(StateActionMemoryMcI memory, Settings settings) {
        return new PolicyDice(memory, settings);
    }

    @Override
    public ActionMcI chooseAction(StateMcI state) {
        return (isRandomAction())
                ? ActionDice.random()
                : getBestActionDice(state);
    }


    private boolean isRandomAction() {
        return RandUtils.randomNumberBetweenZeroAndOne() < settings.probRandomAction();
    }

    private ActionDice getBestActionDice(StateMcI state) {
        double valueThrow = memory.read(state, ActionDice.T);
        double valueStop = memory.read(state, ActionDice.S);
        return valueThrow > valueStop
                ? ActionDice.T
                : ActionDice.S;
    }

}
