package chapters.ch5.implem.walk;

import chapters.ch5.domain.environment.StateMcI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateWalk implements StateMcI {

    VariablesWalk variables;

    public static StateWalk of(int x) {
        return new StateWalk(VariablesWalk.of(x));
    }

    int x() {
        return variables.x();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return variables.equals(((StateWalk) o).variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x());
    }


    @Override
    public String toString() {
        return variables.toString();
    }

}
