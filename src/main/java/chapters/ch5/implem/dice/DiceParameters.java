package chapters.ch5.implem.dice;

import chapters.ch5.domain.environment.ParametersMcI;
import chapters.ch5.domain.environment.StateMcI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DiceParameters implements ParametersMcI {

    public static DiceParameters create() {
        return new DiceParameters();
    }

    @Override
    public boolean isTerminalNonFail(StateMcI state) {
        var stateDice = (StateDice) state;
        return stateDice.count() >= 2;
    }

    @Override
    public boolean isFail(StateMcI state) {
        var stateDice = (StateDice) state;
        return stateDice.sum() > 8;
    }

    public int maxScore() {
        return 6;
    }

    public int minScore() {
        return 1;
    }
}
