package ch3;

import chapters.ch3.factory.EvaluatorDependenciesFactory;
import chapters.ch3.factory.StateValueMemoryPlotterFactory;
import chapters.ch3.implem.splitting_path_problem.*;
import chapters.ch3.policies.SplittingPathPolicyI;
import chapters.ch3.policies.SplittingPathPolicyOptimal;
import chapters.ch3.policies.SplittingPathPolicyRandom;
import chapters.ch3.implem.splitting_path_problem.EvaluatorDependencies;
import core.foundation.config.ConfigFactory;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * startStateSupplier(StartStateSupplierGridMostLeftSplitting.create())  //will not work, only fitted from start state
 */

public class RunnerPolicyEvaluatorSplittingPath {
    enum Policy {RANDOM, OPTIMAL}
    static Policy polChosen = Policy.OPTIMAL;  //change if desired
    static final int NOF_FITS = 1_000; //1000, 50_000;
    static Map<Policy, SplittingPathPolicyI> policesMap;


    @SneakyThrows
    public static void main(String[] args) {
        var dep = EvaluatorDependenciesFactory.produce(NOF_FITS);
        initPolicyMap(dep.environment().getParameters());
        var policy = policesMap.get(polChosen);
        getEvaluator(dep).evaluate(policy);
        plotting(dep);
    }

    private static PolicyEvaluatorSplittingPath getEvaluator(EvaluatorDependencies dep) {
        return PolicyEvaluatorSplittingPath.of(dep);
    }

    private static void initPolicyMap(EnvironmentParametersSplitting parameters) {
        policesMap = Map.of(
                Policy.RANDOM, SplittingPathPolicyRandom.of(parameters),
                Policy.OPTIMAL, SplittingPathPolicyOptimal.of(parameters));
    }

    private static void plotting(EvaluatorDependencies dependencies) {
        var parameters=dependencies.environment().getParameters();
        var mem=dependencies.memory();
        var plotter= StateValueMemoryPlotterFactory.produce(parameters, mem);
        var fileName = "splitting_path_values"+ polChosen.name();
        var picPath = ConfigFactory.pathPicsConfig().ch3();
        var plotCfg=ConfigFactory.plotConfig();
        plotter.plotAndSaveStateValues(picPath,fileName,plotCfg);
    }
}
