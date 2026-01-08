package chapters.ch3.implem.splitting_path_problem;

import core.foundation.util.math.LogarithmicDecay;
import core.gridrl.StartStateSupplierGridI;
import core.gridrl.StateValueMemoryGrid;
import lombok.Builder;

import java.util.List;

@Builder
public record EvaluatorDependencies(
         EnvironmentSplittingPath environment,
         StateValueMemoryGrid memory,
         int nFits,
         LogarithmicDecay learningRate,
         double discountFactor,
         StartStateSupplierGridI startStateSupplier,
         List<Double>errorList
) {
}
    