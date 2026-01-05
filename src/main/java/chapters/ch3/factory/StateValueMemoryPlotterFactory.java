package chapters.ch3.factory;

import chapters.ch3.implem.splitting_path_problem.EnvironmentParametersSplitting;
import chapters.ch3.plotting.StateValueMemoryPlotter;
import core.gridrl.StateValueMemoryGrid;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StateValueMemoryPlotterFactory {

    public static StateValueMemoryPlotter produce(EnvironmentParametersSplitting parameters,
                                                  StateValueMemoryGrid memory) {
        return StateValueMemoryPlotter.builder()
                .memory(memory)
                .parameters(parameters)
                .build();
    }

}
