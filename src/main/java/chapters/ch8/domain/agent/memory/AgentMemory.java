package chapters.ch8.domain.agent.memory;

import chapters.ch8.domain.agent.param.AgentParkingParameters;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.StateParking;
import core.foundation.util.math.MathUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents the memory of an agent in a reinforcement learning environment.
 * This class stores the values of state-action pairs and provides methods for reading and updating these values.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AgentMemory {

    private final AgentParkingParameters agentParameters;
    private final Map<StateActionParking, Double> mapValues;

    public static AgentMemory of(AgentParkingParameters agentParameters) {
        return new AgentMemory(agentParameters, new HashMap<>());
    }

    public double read(StateParking s, ActionParking a) {
        return read(StateActionParking.of(s, a));
    }

    public int getNumberOfFittedStateActions() {
        return mapValues.size();
    }


    public Map<ActionParking,Double> readActionValues(StateParking s) {
        return ActionParking.allActions().stream()
                .collect(Collectors.toMap(
                        a -> a,
                        action -> read(s, action)));
    }

    public double value(StateParking s) {
        return Collections.max(readActionValues(s).values());
    }

    public double fit(StateActionParking sa, double valueTar, double learningRate) {
        double tdMax=agentParameters.tdMax();
        var valOld = read(sa);
        double err0 = valueTar - valOld;
        double clippedError = MathUtil.clip(err0,-tdMax,tdMax);
        write(sa, valOld + learningRate * clippedError);
        return err0;
    }

    public void write(StateActionParking sa, double value) {
        mapValues.put(sa, value);
    }

    public double read(StateActionParking sa) {
        return mapValues.getOrDefault(sa, agentParameters.defaultValueStateAction());
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        sb.append("MemoryGrid{");
        for (var entry : mapValues.entrySet()) {
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(read(entry.getKey()));
            sb.append(" ").append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

}
