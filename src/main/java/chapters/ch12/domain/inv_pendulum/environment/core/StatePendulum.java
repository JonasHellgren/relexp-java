package chapters.ch12.domain.inv_pendulum.environment.core;

import core.foundation.util.formatting.NumberFormatterUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state of an inverted pendulum.
 * This class encapsulates the nOccupied, angular speed, and number of steps of the pendulum.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatePendulum {
    public static final int NOF_DIGITS = 3;
    public static final int ZERO_STEPS = 0;
    public static final int IDX_ANGLE = 0;
    public static final int IDX_SPD = 1;

    VariablesPendulum variables;

    public static StatePendulum ofStart(double angle, double angularSpeed) {
        return of(angle, angularSpeed, ZERO_STEPS);
    }

    public static StatePendulum of(double angle, double angularSpeed, int nSteps) {
        return new StatePendulum(new VariablesPendulum(angle, angularSpeed,nSteps));
    }

    public double angle() {
        return variables.angle();
    }

    public double angularSpeed() {
        return variables.angularSpeed();
    }

    public int nSteps() {
        return variables.nSteps();
    }

    public static List<StatePendulum> getStatePendulumList(List<List<Double>> listOfLists) {
        List<StatePendulum> stateList = new ArrayList<>();
        for (List<Double> stateValues : listOfLists) {
            stateList.add(StatePendulum.ofStart(stateValues.get(IDX_ANGLE), stateValues.get(IDX_SPD)));
        }
        return stateList;
    }

    public List<Double> asList() {
        return List.of(variables.angle(), variables.angularSpeed());
    }

    @Override
    public String toString() {
        return "State{" +
                "nOccupied=" +
                NumberFormatterUtil.getRoundedNumberAsString(variables.angle(), NOF_DIGITS) +
                ", angularSpeed=" +
                NumberFormatterUtil.getRoundedNumberAsString(variables.angularSpeed(), NOF_DIGITS) +
                '}';
    }

}
