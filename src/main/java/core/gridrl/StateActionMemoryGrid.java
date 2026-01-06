package core.gridrl;

import com.google.common.base.Preconditions;
import core.foundation.util.math.MyMathUtils;
import lombok.AllArgsConstructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a memory grid for an agent in a grid environment.
 * This class stores the value of each state-action pair and provides methods for reading and updating these values.
 */
@AllArgsConstructor
public class StateActionMemoryGrid {

    private final AgentGridParameters agentParameters;
    private final InformerGridParamsI informerParams;
    private final Map<StateActionGrid, Double> mapValue;

    public static StateActionMemoryGrid of(AgentGridParameters agentParameters,
                                           InformerGridParamsI informerParams) {
        return new StateActionMemoryGrid(agentParameters, informerParams, new HashMap<>());
    }

    /**
     * Reads the value of the given state-action pair.
     *
     * @param state the state
     * @param action the action
     * @return the value of the state-action pair
     */
    public double read(StateGrid state, ActionGrid action) {
        return read(StateActionGrid.of(state, action));
    }

    /**
     * Reads the value of the given state-action pair.
     *
     * @param sa the state-action pair
     * @return the value of the state-action pair, or the default value if it has not been set
     */
    public double read(StateActionGrid sa) {
        return mapValue.containsKey(sa)
                ? mapValue.get(sa)
                : agentParameters.defaultValueStateAction();
    }

    /**
     * Returns the number of state-action pairs that have been fitted.
     *
     * @return the number of fitted state-action pairs
     */
    public int getNumberOfFittedStateActions() {
        return mapValue.size();
    }

    /**
     * Reads the values of all valid actions in the given state.
     *
     * @param state the state
     * @return a map of actions to their values
     */
    public Map<ActionGrid, Double> readActionValuesInState(StateGrid state) {
        return informerParams.getValidActions().stream()
                .collect(Collectors.toMap(
                        a -> a,
                        a -> read(StateActionGrid.of(state, a))));
    }


    /**
     * Returns the value of the best action in the given state.
     *
     * @param state the state
     * @return the value of the best action
     */
    public Double getValueOfBestAction(StateGrid state) {
        var actionValueMap = readActionValuesInState(state);
        return Collections.max(actionValueMap.values());
    }

    /**
     * Fits the value of the given state-action pair to the target value.
     *
     * @param state the state
     * @param action the action
     * @param valueTar the target value
     * @param learningRate the learning rate
     * @return the error
     */
    public double fit(StateGrid state, ActionGrid action, double valueTar, double learningRate) {
        return fit(StateActionGrid.of(state, action), valueTar, learningRate);
    }

    /**
     * Fits the value of the given state-action pair to the target value.
     *
     * @param sa the state-action pair
     * @param valueTar the target value
     * @param learningRate the learning rate
     * @return the error
     */
    public double fit(StateActionGrid sa, double valueTar, double learningRate) {
        double tdMax=agentParameters.tdMax();
        Preconditions.checkArgument(tdMax>0,"TD error clipping threshold must be positive");
        var valOld = read(sa);
        double err = MyMathUtils.clip(valueTar - valOld,-tdMax,tdMax);
        write(sa, valOld + learningRate * err);
        return err;
    }

    /**
     * Writes the given value to the given state-action pair.
     *
     * @param sa the state-action pair
     * @param value the value
     */
    public void write(StateActionGrid sa, double value) {
        mapValue.put(sa, value);
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
