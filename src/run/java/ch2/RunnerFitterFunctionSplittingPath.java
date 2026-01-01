package ch2;

import chapters.ch2.implem.splitting_path_problem.*;
import core.foundation.config.ProjectPropertiesReader;
import core.foundation.util.math.LogarithmicDecay;
import core.gridrl.StateGrid;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * startStateSupplier(StartStateSupplierGridMostLeftSplitting.create())  //will not work, only fitted from start state
 */

public class RunnerFitterFunctionSplittingPath {
    public static final int IDX_POLICY = 1;  //0=Random, 1=Optimal

    public static final Pair<Double, Double> LEARNING_RATE = Pair.create(0.01, 0.01);  //0.1
    public static final double DISCOUNT_FACTOR = 1.0;  //0.5
    public static final int NOF_FITS = 1_000; //1000, 50_000;
    private static EnvironmentSplittingPath environment;
    private static List<SplittingPathPolicyI> polices;
    private static List<String> policyNames;
    private static StateValueMemoryGrid memory;
    private static Set<StateGrid> allStates;

    @SneakyThrows
    public static void main(String[] args) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        initFields(parameters);
        var policy = polices.get(IDX_POLICY);
        var fitter = getMemoryFitterSplittingPath();
        fitter.fitMemory(policy);
        plotAndSave(parameters);
    }

    private static MemoryFitterSplittingPath getMemoryFitterSplittingPath() {
        var pair = LEARNING_RATE;
        var decayingLearningRate = LogarithmicDecay.of(pair.getFirst(), pair.getSecond(), NOF_FITS);
        return MemoryFitterSplittingPath.builder()
                .environment(environment).memory(memory)
                .startStateSupplier(StartStateSupplierGridRandomSplitting.create())
                .nFits(NOF_FITS).learningRate(decayingLearningRate).discountFactor(DISCOUNT_FACTOR)
                .build();
    }

    private static void plotAndSave(EnvironmentParametersSplitting parameters) throws IOException {
        var pathPics = ProjectPropertiesReader.create().pathConceptsPics();
        var plotter = StateValueMemoryPlotter.builder()
                .filePath(pathPics)
                .fileNameAddOn("splitting_path_" + policyNames.get(IDX_POLICY))
                .memory(memory)
                .parameters(parameters)
                .build();
        plotter.plotAndSaveStateValues();
    }


    private static void initFields(EnvironmentParametersSplitting parameters) {
        environment = EnvironmentSplittingPath.of(parameters);
        var policyRandom = SplittingPathPolicyRandom.of(parameters);
        var policyOptimal = SplittingPathPolicyOptimal.of(parameters);
        polices = List.of(policyRandom, policyOptimal);
        policyNames = List.of("Random", "Optimal");
        memory = StateValueMemoryGrid.createZeroDefault();
        allStates = new HashSet<>(parameters.getStatesExceptSplit());
        allStates.add(parameters.getSplitState().iterator().next());
    }
}
