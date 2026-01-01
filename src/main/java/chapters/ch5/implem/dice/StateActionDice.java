package chapters.ch5.implem.dice;

import chapters.ch5.domain.memory.StateActionMcI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.Objects;

/**
 * Represents a state-action pair in the context of a dice game.
 * Used by StateActionMemoryDice to access value of a state-action pair.
 * This class implements the StateActionMcI interface and provides methods for creating and manipulating state-action pairs.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateActionDice implements StateActionMcI {

    StateDice state;
    ActionDice action;

    public static StateActionDice of(StateDice state, ActionDice action) {
        return new StateActionDice(state, action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return state.equals(((StateActionDice) o).state) &&
                action.equals(((StateActionDice) o).action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state.hashCode(), action.hashCode());
    }

    @Override
    public String toString() {
        return "s="+state.toString()+", a="+action.toString();
    }

}
