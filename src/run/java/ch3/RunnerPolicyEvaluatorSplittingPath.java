package ch3;

import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import chapters.ch3.factory.StateValueMemoryPlotterFactory;
import chapters.ch3.implem.splitting_path_problem.*;
import chapters.ch3.policies.SplittingPathPolicyI;
import chapters.ch3.policies.SplittingPathPolicyOptimal;
import chapters.ch3.policies.SplittingPathPolicyRandom;
import core.foundation.config.ConfigFactory;
import core.foundation.util.math.LogarithmicDecay;
import core.gridrl.StateValueMemoryGrid;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;

import java.util.Map;

/**
 * startStateSupplier(StartStateSupplierGridMostLeftSplitting.create())  //will not work, only fitted from start state
 */

public class RunnerPolicyEvaluatorSplittingPath {
    enum Policy {RANDOM, OPTIMAL}
    static Policy polChosen = Policy.OPTIMAL;  //change if desired

    static final Pair<Double, Double> LEARNING_RATE = Pair.create(0.01, 0.01);  //0.1
    static final double DISCOUNT_FACTOR = 1.0;  //0.5
    static final int NOF_FITS = 1_000; //1000, 50_000;
    static EnvironmentSplittingPath environment;
    static Map<Policy, SplittingPathPolicyI> policesMap;
    static StateValueMemoryGrid memory;

    @SneakyThrows
    public static void main(String[] args) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        initFields(parameters);
        var policy = policesMap.get(polChosen);
        getEvaluator().evaluate(policy);
        var plotter= StateValueMemoryPlotterFactory.produce(parameters,memory);
        var name = "splitting_path_values"+ polChosen.name();
        plotter.plotAndSaveStateValues(ConfigFactory.pathPicsConfig().ch3(),name);

    }

    private static PolicyEvaluatorSplittingPath getEvaluator() {
        var pair = LEARNING_RATE;
        var decayingLearningRate = LogarithmicDecay.of(pair.getFirst(), pair.getSecond(), NOF_FITS);
        return PolicyEvaluatorSplittingPath.builder()
                .environment(environment).memory(memory)
                .startStateSupplier(StartStateSupplierGridRandomSplitting.create())
                .nFits(NOF_FITS).learningRate(decayingLearningRate).discountFactor(DISCOUNT_FACTOR)
                .build();
    }



    private static void initFields(EnvironmentParametersSplitting parameters) {
        environment = EnvironmentSplittingPath.of(parameters);
        policesMap = Map.of(
                Policy.RANDOM, SplittingPathPolicyRandom.of(parameters),
                Policy.OPTIMAL, SplittingPathPolicyOptimal.of(parameters));
        memory = StateValueMemoryGrid.createZeroDefault();
    }
}
