package ch5;

import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import chapters.ch3.implem.splitting_path_problem.*;
import chapters.ch3.policies.SplittingPathPolicyOptimal;
import chapters.ch3.implem.splitting_path_problem.EvaluatorDependencies;
import chapters.ch5.domain.policy_evaluator.EvaluatorSettings;
import chapters.ch5.domain.policy_evaluator.StatePolicyEvaluationMc;
import chapters.ch5.factory.SplittingDependenciesFactory;
import chapters.ch5.implem.splitting.StartStateSupplierRandomSplitting;
import core.foundation.config.ConfigFactory;
import core.foundation.gadget.math.LogarithmicDecay;
import core.foundation.gadget.math.MovingAverage;
import core.gridrl.StateValueMemoryGrid;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;
import core.plotting_rl.chart.ManyLinesFactory;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.List;

public class RunnerPlotErrorVersusIterationForSplittingPath {

    public static final int SPACE_BETWEEN_X_TICKS = 2500;
    public static final int LENGTH_WINDOW = 100;
    public static final int N_ITERATIONS_MC = 20_000;
    public static final int N_FITS_MAX_PLOTTING = 10_000;
    public static final double LEARNING_RATE = 0.01;

    @SneakyThrows
    public static void main(String[] args) {
        var settings = getSettings();
        var errorListMc = getErrorListMc(settings);
        var creator = getChartCreator(errorListMc);
        creator.addLine("Monte carlo",errorListMc);
        creator.addLine("Policy evaluation",getErrorListTd(settings));
        saveAndPlot(creator);
    }

    private static void saveAndPlot(ManyLinesChartCreator creator) {
        var chart = creator.create();
        var styler=chart.getStyler();
        styler.setLegendPosition(Styler.LegendPosition.InsideNE);
        ChartSaverAndPlotter.showChartSaveInFolderMonteCarlo(chart, "error-vs-fit-mc-td");
    }

    private static EvaluatorSettings getSettings() {
        return SplittingDependenciesFactory.SETTINGS
                .withNIterations(N_ITERATIONS_MC)
                .withStartAndEndLearningRate(Pair.create(LEARNING_RATE,LEARNING_RATE));
    }

    private static ManyLinesChartCreator getChartCreator(List<Double> errorListMc) {
        var factory = ManyLinesFactory.builder()
                .spaceBetweenXTicks(SPACE_BETWEEN_X_TICKS)
                .nItems(errorListMc.size())
                .xLabel("Iteration")
                .yLabel("Error")
                .build();
        var plotCfg = ConfigFactory.plotConfig();
        return factory.getManyLinesChartCreator(plotCfg);
    }

    private static List<Double> getErrorListMc(EvaluatorSettings settings) {
        var dep=SplittingDependenciesFactory.optimalPolicy(settings)
                .withStartStateSupplier(StartStateSupplierRandomSplitting.create());
        var evaluator= StatePolicyEvaluationMc.of(dep);
        evaluator.evaluate();
        var errorList0= evaluator.getDependencies().errorList();
        return getFilteredSubList(errorList0);
    }

    private static List<Double> getErrorListTd(EvaluatorSettings settings) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var environment = EnvironmentSplittingPath.of(parameters);
        var policyTd = SplittingPathPolicyOptimal.of(parameters);
        var pair = settings.startAndEndLearningRate();
        var decayingLearningRate = LogarithmicDecay.of(
                pair.getFirst(),pair.getSecond(),settings.nIterations());
        var memory = StateValueMemoryGrid.createZeroDefault();
        var dep= EvaluatorDependencies.builder()
                .environment(environment).memory(memory)
                .startStateSupplier(StartStateSupplierGridRandomSplitting.create())
                .nFits(N_FITS_MAX_PLOTTING).learningRate(decayingLearningRate).discountFactor(settings.gamma())
                .errorList(new ArrayList<>())
                .build();
        var evaluator= PolicyEvaluatorSplittingPath.of(dep);
/*
        var evaluator= PolicyEvaluatorSplittingPath.builder()
                .environment(environment)
                .memory(memory)
                .startStateSupplier(StartStateSupplierGridRandomSplitting.create())
                .nFits(N_FITS_MAX_PLOTTING)
                .learningRate(decayingLearningRate)
                .discountFactor(settings.gamma())
                .build();*/
        evaluator.evaluate(policyTd);
        var errorListTd0=evaluator.getDependencies().errorList();
        return getFilteredSubList(errorListTd0);
    }


    private static List<Double> getFilteredSubList(List<Double> errorList0) {
        var errorList0Cut= errorList0.subList(0, N_FITS_MAX_PLOTTING);
        var movingAverage = new MovingAverage(LENGTH_WINDOW, errorList0Cut);
        return movingAverage.getFiltered();
    }

}
