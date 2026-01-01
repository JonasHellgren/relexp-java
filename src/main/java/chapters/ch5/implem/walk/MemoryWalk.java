package chapters.ch5.implem.walk;

import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.memory.StateMemoryMcI;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.HashMap;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryWalk implements StateMemoryMcI {

    public static final double DEFAULT_VALUE = 0.0;
    HashMap<StateWalk,Double> stateValueMap;

    public static MemoryWalk create() {
        return new MemoryWalk(new HashMap<>());
    }


    @Override
    public double read(StateMcI state) {
        checkStateType(state);
        return stateValueMap.getOrDefault(state, DEFAULT_VALUE);
    }

    @Override
    public void write(StateMcI state, double value) {
        checkStateType(state);
        stateValueMap.put((StateWalk) state, value);
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

    private static void checkStateType(StateMcI state) {
        Preconditions.checkArgument(state instanceof StateWalk);
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        for (var entry : stateValueMap.entrySet()) {
            var state = entry.getKey();
            var value = entry.getValue();
            sb.append(state).append(" : ").append(value).append("\n");
        }
        return sb.toString();
    }

}
