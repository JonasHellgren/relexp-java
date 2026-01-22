package chapters.ch5.implem.dice;

import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.EnvironmentMcI;
import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.environment.StepReturnMc;
import com.google.common.base.Preconditions;
import core.foundation.util.rand.RandUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * EnvironmentDice is a Monte Carlo environment that simulates a game of dice.
 * It implements the EnvironmentMcI interface and provides methods for stepping through the game.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentDice implements EnvironmentMcI {

    public static final String NAME = "Dice";

    DiceParameters parameters;

    public static EnvironmentDice create() {
        return new EnvironmentDice(DiceParameters.create());
    }
    
    @Override
    public String name() {
        return NAME;
    }

    /**
     * Steps through the game, given the current state and action.
     *
     * @param state the current state of the game
     * @param action the action to take
     * @return the result of the step, including the new state, reward, and whether the game is terminal
     */
    @Override
    public StepReturnMc step(StateMcI state, ActionMcI action) {
        Preconditions.checkArgument(state instanceof StateDice, "invalid class of state=" + state);
        Preconditions.checkArgument(action instanceof ActionDice, "invalid class of action=" + action);
        var stateDice = (StateDice) state;
        var actionDice = (ActionDice) action;
        int newSum = stateDice.sum() + actionDice.increaseInSum(getScore(actionDice));
        int newCount = stateDice.count() + actionDice.deltaCount();
        boolean isFail = parameters.isFail(StateDice.of(newSum, newCount));
        var newState = getNewState(isFail, newCount, newSum);
        boolean isTerminal = isTerminal(actionDice, newState, isFail);
        double gameScore = getGameScore(isTerminal, newState);
        return StepReturnMc.builder()
                .sNext(newState)
                .reward(gameScore)
                .isFail(isFail)
                .isTerminal(isTerminal)
                .build();
    }

    private int getScore(ActionDice actionDice) {
        return actionDice.isStopThrowing()
                ? 0
                : RandUtil.getRandomIntNumber(parameters.minScore(),parameters.maxScore()+1);
    }

    private static StateDice getNewState(boolean isFail, int newCount, int newSum) {
        return isFail
                ?  StateDice.of(0, newCount)
                : StateDice.of(newSum, newCount);
    }

    private boolean isTerminal(ActionDice actionDice, StateDice newState, boolean isFail) {
        return  actionDice.isStopThrowing() ||
                parameters.isTerminalNonFail(newState) ||
                isFail;
    }


    private  double getGameScore(boolean isTerminal, StateDice newState) {
        return isTerminal ? newState.sum() :0;
    }


}
