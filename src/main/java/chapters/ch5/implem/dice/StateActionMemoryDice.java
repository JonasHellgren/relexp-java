package chapters.ch5.implem.dice;

import chapters.ch5.domain.environment.ActionMcI;
import chapters.ch5.domain.environment.StateMcI;
import chapters.ch5.domain.memory.StateActionMcI;
import chapters.ch5.domain.memory.StateActionMemoryMcI;
import com.google.common.base.Preconditions;
import core.foundation.gadget.normal_distribution.NormalSampler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.HashMap;

/**
 * A state-action memory implementation for the dice environment.
 * This class stores the values of state-action pairs and provides methods to read and write these values.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateActionMemoryDice implements StateActionMemoryMcI {

    public static final double DEFAULT_VALUE_MEAN = 4.0, DEFAULT_VALUE_STD = 0.0;
    HashMap<StateActionDice, Double> stateActionValueMap;
    NormalSampler sampler;

    public static StateActionMemoryDice create() {
        return new StateActionMemoryDice(
                new HashMap<>(),
                new NormalSampler(DEFAULT_VALUE_MEAN, DEFAULT_VALUE_STD));
    }

    @Override
    public double read(StateMcI state, ActionMcI action) {
        checkTypes(state, action);
        return stateActionValueMap.getOrDefault(
                StateActionDice.of((StateDice) state, (ActionDice) action),
                sampler.generateSample());
    }

    @Override
    public void write(StateMcI state, ActionMcI action, double value) {
        checkTypes(state, action);
        stateActionValueMap.put(StateActionDice.of((StateDice) state, (ActionDice) action), value);
    }

    @Override
    public boolean isEmpty() {
        return stateActionValueMap.isEmpty();
    }

    @Override
    public boolean isPresent(StateActionMcI stateAction) {
        return stateActionValueMap.containsKey((StateActionDice) stateAction);
    }

    @Override
    public int size() {
        return stateActionValueMap.size();
    }

    private static void checkTypes(StateMcI state, ActionMcI action) {
        Preconditions.checkArgument(state instanceof StateDice);
        Preconditions.checkArgument(action instanceof ActionDice);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var entry : stateActionValueMap.entrySet()) {
            var sa = entry.getKey();
            var value = entry.getValue();
            sb.append(sa).append(" : ").append(value).append("\n");
        }
        return sb.toString();
    }

}
