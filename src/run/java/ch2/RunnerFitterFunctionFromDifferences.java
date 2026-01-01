package ch2;

import chapters.ch2.factory.ChartFactory;
import chapters.ch2.factory.FitterFunctionFactory;
import chapters.ch2.factory.FittingParametersFactory;
import chapters.ch2.implem.function_fitting.FitterFunctionDifferences;
import chapters.ch2.implem.function_fitting.FitterOutCalculator;
import chapters.ch2.implem.function_fitting.FittingParameters;
import com.google.common.collect.Range;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.math.SigmoidFunctions;
import core.foundation.util.rand.RandUtils;

import java.util.List;
import java.util.stream.IntStream;

import static core.plotting.chart_plotting.ChartSaverAndPlotter.showChartSaveInFolderConcepts;


public class RunnerFitterFunctionFromDifferences {

    public static final int N_EPOCHS = 1000;
    public static final int N_POINTS_PLOTTING = 100;
    public static final String FILE_NAME = "sigmoid_fitted_differences";
    public static final int N_SAMPLES = 10;

    public static void main(String[] args) {
        var parameters = FittingParametersFactory.produceDefault();
        var fitter = FitterFunctionFactory.produceDifferences(parameters);
        fitMemoryInFitter(parameters, fitter);
        var xList = parameters.getXList(N_POINTS_PLOTTING);
        var yList = FitterOutCalculator.produceOutput(fitter, xList, parameters);
        showChartSaveInFolderConcepts(ChartFactory.getChart(xList, yList), FILE_NAME);
    }

    private static void fitMemoryInFitter(FittingParameters parameters,
                                          FitterFunctionDifferences fitter) {
        IntStream.range(0, N_EPOCHS).forEach((i) -> {
            var data = getTrainData(parameters, fitter);
            fitter.fit(data);
        });
    }

    private static TrainData getTrainData(FittingParameters parameters,
                                          FitterFunctionDifferences fitter) {
        var data = TrainData.emptyFromErrors();
        double dx = parameters.deltaX();
        double range = parameters.range();
        var rangeClosed = Range.closed(-range, range);
        for (int i = 0; i < N_SAMPLES; i++) {
            double xRand = RandUtils.doubleInInterval(-range, range);
            double xRandPlusDx = xRand + dx;
            if (rangeClosed.contains(xRandPlusDx)) {
                double yApproxForXRand = fitter.read(xRand);
                double yApproxForXRandPlusDx = fitter.read(xRandPlusDx);
                double derSigmoidForXrand = SigmoidFunctions.derSigmoid.applyAsDouble(xRand);
                double dy = derSigmoidForXrand * dx;
                double error = yApproxForXRand + dy - yApproxForXRandPlusDx;
                data.addInAndError(List.of(xRandPlusDx), error);
            }
        }
        return data;
    }


}
