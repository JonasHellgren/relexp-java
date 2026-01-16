package chapters.ch5.implem.converter;

import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.implem.dice.StateDice;
import chapters.ch5.implem.splitting.StateSplittingMc;
import chapters.ch5.implem.walk.StateWalk;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StateTypeConverter {

    public static StateDice toDice(StateMcI state) {
        if (state instanceof StateDice s) {
            return s;
        }
        throw new IllegalArgumentException(
                "Expected StateDice, got: " + state.getClass().getName());
    }

    public static StateWalk toWalk(StateMcI state) {
        if (state instanceof StateWalk s) {
            return s;
        }
        throw new IllegalArgumentException(
                "Expected StateWalk, got: " + state.getClass().getName());
    }

    public static StateSplittingMc toSplit(StateMcI state) {
        if (state instanceof StateSplittingMc s) {
            return s;
        }
        throw new IllegalArgumentException(
                "Expected StateSplittingMc, got: " + state.getClass().getName());
    }


}
