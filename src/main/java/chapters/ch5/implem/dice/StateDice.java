package chapters.ch5.implem.dice;

import chapters.ch5.domain.environment.StateMcI;
import core.foundation.util.math.MathUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.Objects;

/**
 * Represents the state of a dice environment in a Monte Carlo simulation.
 */

 @AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateDice implements StateMcI {

    VariablesDice variables;

    public static StateDice of(int sum, int count) {
        return new StateDice(VariablesDice.of(sum, count));
    }

    public static StateDice start() {
        return new StateDice(VariablesDice.of(0, 0));
    }

    public int sum() {
        return variables.sum();
    }

    public int count() {
        return variables.count();
    }

    public boolean isValid() {
        return variables.count() == 0 && variables.sum() == 0  ||
                variables.count() == 1 && MathUtil.isInLimits(variables.sum(), 1, 6);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return variables.equals(((StateDice) o).variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables.hashCode());
    }


    @Override
    public String toString() {
        return variables.toString();
    }

}
