package chapters.ch5.implem.dice;


import chapters.ch5.domain.environment.ActionMcI;
import core.foundation.util.rand.RandUtil;

/**
 * Enum values:
 * T - Throw the dice
 * S - Stop throwing the dice
 */
public enum ActionDice implements ActionMcI {
     T,S;

    public  int increaseInSum(int sum) {
        return T.equals(this) ? sum : 0;
    }

    public  int deltaCount() {
        return T.equals(this) ? 1 : 0;
    }

    public boolean isStopThrowing() {
        return S.equals(this);
    }

    public static ActionDice random() {
        int randIndex = RandUtil.getRandomIntNumber(0,ActionDice.values().length);
        return ActionDice.values()[randIndex];
    }

}
