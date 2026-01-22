package chapters.ch3.factory;

import chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath;
import chapters.ch3.implem.splitting_path_problem.EvaluatorDependencies;
import chapters.ch3.implem.splitting_path_problem.StartStateSupplierGridRandomSplitting;
import core.foundation.gadget.math.LogarithmicDecay;
import core.gridrl.StateValueMemoryGrid;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;

@UtilityClass
public class EvaluatorDependenciesFactory {

    static final Pair<Double, Double> LEARNING_RATE = Pair.create(0.01, 0.01);  //0.1
    static final double DISCOUNT_FACTOR = 1.0;  //0.5


    public static EvaluatorDependencies produce(int nFits) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var environment = EnvironmentSplittingPath.of(parameters);
        var  memory = StateValueMemoryGrid.createZeroDefault();
        var pair = LEARNING_RATE;
        var decayingLearningRate = LogarithmicDecay.of(pair.getFirst(), pair.getSecond(), nFits);
        var dep= EvaluatorDependencies.builder()
                .environment(environment).memory(memory)
                .startStateSupplier(StartStateSupplierGridRandomSplitting.create())
                .nFits(nFits).learningRate(decayingLearningRate).discountFactor(DISCOUNT_FACTOR)
                .errorList(new ArrayList<>())
                .build();
        return dep;
    }

}
