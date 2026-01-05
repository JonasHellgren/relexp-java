package ch5;

import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import chapters.ch3.implem.splitting_path_problem.*;
import chapters.ch3.policies.SplittingPathPolicyOptimal;
import chapters.ch5._shared.evaluation.Settings;
import chapters.ch5._shared.evaluation.StatePolicyEvaluationMc;
import chapters.ch5._shared.factory.StatePolicyEvaluationFactory;
import chapters.ch5.implem.splitting.StartStateSupplierRandomSplitting;
import core.foundation.util.math.LogarithmicDecay;
import core.foundation.util.math.MovingAverage;
import core.gridrl.StateValueMemoryGrid;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import core.plotting.chart_plotting.PlotterFactory;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;
import org.knowm.xchart.style.Styler;
import java.io.IOException;
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

    private static Settings getSettings() {
        return StatePolicyEvaluationMc.DEFAULT_SETTINGS
                .withNIterations(N_ITERATIONS_MC)
                .withStartAndEndLearningRate(Pair.create(LEARNING_RATE,LEARNING_RATE));
    }

    private static ManyLinesChartCreator getChartCreator(List<Double> errorListMc) throws IOException {
        var factory = PlotterFactory.builder()
                .spaceBetweenXTicks(SPACE_BETWEEN_X_TICKS)
                .nItems(errorListMc.size())
                .xLabel("Iteration")
                .yLabel("Error")
                .build();
        return factory.getManyLinesChartCreator();
    }

    private static List<Double> getErrorListMc(Settings settings) {
        var policyMc = StatePolicyEvaluationFactory.
                createSplittingOptimalPolicy(settings);
        policyMc.setStartStateSupplier(StartStateSupplierRandomSplitting.create());
        policyMc.evaluate();
        var errorList0= policyMc.getErrorList();
        return getFilteredSubList(errorList0);
    }

    private static List<Double> getErrorListTd(Settings settings) {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        var environment = EnvironmentSplittingPath.of(parameters);
        var policyTd = SplittingPathPolicyOptimal.of(parameters);
        var pair = settings.startAndEndLearningRate();
        var decayingLearningRate = LogarithmicDecay.of(
                pair.getFirst(),pair.getSecond(),settings.nIterations());
        var memory = StateValueMemoryGrid.createZeroDefault();
        var fitter= PolicyEvaluatorSplittingPath.builder()
                .environment(environment)
                .memory(memory)
                .startStateSupplier(StartStateSupplierGridRandomSplitting.create())
                .nFits(N_FITS_MAX_PLOTTING)
                .learningRate(decayingLearningRate)
                .discountFactor(settings.gamma())
                .build();
        fitter.evaluate(policyTd);
        var errorListTd0=fitter.getErrorList();
        return getFilteredSubList(errorListTd0);
    }


    private static List<Double> getFilteredSubList(List<Double> errorList0) {
        var errorList0Cut= errorList0.subList(0, N_FITS_MAX_PLOTTING);
        var movingAverage = new MovingAverage(LENGTH_WINDOW, errorList0Cut);
        return movingAverage.getFiltered();
    }

}
