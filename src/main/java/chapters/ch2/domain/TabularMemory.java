package chapters.ch2.domain;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Discrete/tabular memory, primary for value functions
 * Used by FitterSingleParameter
 */

@AllArgsConstructor
public class TabularMemory {

    private final FittingParameters parameters;
    private final Map<Integer,Double> memory;

    public static TabularMemory of(FittingParameters parameters) {
        return new TabularMemory(parameters, new HashMap<>());
    }

    public double read(int index) {
        return memory.getOrDefault(index, parameters.defaultMemoryValue());
    }

    public void write(int index, double value) {
        memory.put(index, value);
    }

    public int size() {
        return memory.size();
    }

}
