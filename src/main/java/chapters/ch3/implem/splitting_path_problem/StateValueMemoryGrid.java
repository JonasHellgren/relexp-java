package chapters.ch3.implem.splitting_path_problem;

import core.gridrl.StateGrid;
import lombok.AllArgsConstructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a memory grid for an agent in a grid environment.
 * This class stores the value of each state and provides methods
 * for reading and updating these values.
 */
@AllArgsConstructor
public class StateValueMemoryGrid {

    double defaultStateValue;
    private final Map<StateGrid, Double> mapValue;

    public static StateValueMemoryGrid createZeroDefault() {
        return new StateValueMemoryGrid(0.0, new HashMap<>());
    }

    public static StateValueMemoryGrid of(double defaultStateValue) {
        return new StateValueMemoryGrid(defaultStateValue, new HashMap<>());
    }


    public int size() {
        return mapValue.size();
    }

    public boolean isEmpty() {
        return mapValue.isEmpty();
    }

    public Set<StateGrid> keySet() {
        return mapValue.keySet();
    }

    /**
     * Reads the value of the given state-action pair.
     *
     * @param sa the state-action pair
     * @return the value of the state-action pair, or the default value if it has not been set
     */
    public double read(StateGrid sa) {
        return mapValue.containsKey(sa)
                ? mapValue.get(sa)
                : defaultStateValue;
    }

    /**
     * Fits the value of the given state-action pair to the target value.
     *
     * @param s the state
     * @param valueTar the target value
     * @param learningRate the learning rate
     * @return the error
     */
    public double fit(StateGrid s, double valueTar, double learningRate) {
        var valOld = read(s);
        double err = valueTar - valOld;
        write(s, valOld + learningRate * err);
        return err;
    }

    /**
     * Writes the given value to the given state-action pair.
     *
     * @param s the state
     * @param value the value
     */
    public void write(StateGrid s, double value) {
        mapValue.put(s, value);
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        sb.append("MemoryGrid{");
        for (var entry : mapValue.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
            sb.append(" ").append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

}
