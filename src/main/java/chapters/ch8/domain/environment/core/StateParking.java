package chapters.ch8.domain.environment.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * Represents a state in the parking environment, which includes the number of occupied parking spots and the current fee.
 * This class also keeps track of the number of steps taken, which is not a real attribute of the environment but rather a tracking mechanism.
 * Note that the agent does not use the number of steps taken for making action decisions.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateParking {
    public static final int ZERO_STEPS = 0;

    VariablesParking variables;

    public static StateParking ofStart(int nOccup, FeeEnum fee) {
        return of(nOccup, fee, ZERO_STEPS);
    }

    public static StateParking of(int nAvail, FeeEnum fee, int nSteps) {
        return new StateParking(new VariablesParking(nAvail, fee, nSteps));
    }

    public int nOccupied() {
        return variables.nOccupied();
    }

    public FeeEnum fee() {
        return variables.fee();
    }

    public int nSteps() {
        return variables.nSteps();
    }

    public StateParking copy() {
        return new StateParking(variables.copy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return variables.equals(((StateParking) o).variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nOccupied(), fee());
    }


    @Override
    public String toString() {
        return variables.toString();
    }


}
