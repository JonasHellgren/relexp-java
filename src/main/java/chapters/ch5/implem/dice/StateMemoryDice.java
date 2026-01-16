package chapters.ch5.implem.dice;

import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.memory.StateMemoryMcI;
import chapters.ch5.implem.converter.StateTypeConverter;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.HashMap;

/**
 * A bit unnecessary due to the existence of StateActionMemoryDice, but needed by
 * the semi-generic class ValueMemoryMcPlotter
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateMemoryDice implements StateMemoryMcI {

    private static final double DEFAULT_VALUE = 0.0;
    HashMap<StateDice, Double> stateValueMap;

    public static StateMemoryDice create() {
        return new StateMemoryDice(new HashMap<>());
    }


    @Override
    public double read(StateMcI state) {
        var s= StateTypeConverter.toDice(state);
        return stateValueMap.getOrDefault(s, DEFAULT_VALUE);
    }

    @Override
    public void write(StateMcI state, double value) {
        var s= StateTypeConverter.toDice(state);
        stateValueMap.put(s, value);
    }

    @Override
    public boolean isEmpty() {
        return stateValueMap.isEmpty();
    }

    @Override
    public boolean isPresent(StateMcI state) {
        return stateValueMap.containsKey(state);
    }

    @Override
    public int size() {
        return stateValueMap.size();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var entry : stateValueMap.entrySet()) {
            var state = entry.getKey();
            var value = entry.getValue();
            sb.append(state).append(" : ").append(value).append("\n");
        }
        return sb.toString();
    }

}
