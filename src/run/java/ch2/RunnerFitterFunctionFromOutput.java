package ch2;

import chapters.ch2.factory.ChartFactory;
import chapters.ch2.factory.FitterFunctionFactory;
import chapters.ch2.factory.FittingParametersFactory;
import core.foundation.gadget.training.TrainDataInOut;
import core.foundation.util.math.SigmoidFunctions;
import core.foundation.util.rand.RandUtils;
import core.plotting.plotting_2d.ChartUtility;
import java.util.List;
import java.util.stream.IntStream;
import static core.foundation.util.collections.ListCreator.createFromStartToEndWithNofItems;
import static core.plotting.chart_plotting.ChartSaverAndPlotter.showChartSaveInFolderConcepts;

public class RunnerFitterFunctionFromOutput {
    public static final int N_EPOCHS = 1000;
    public static final int N_POINTS = 1000;  //100
    public static final String FILE_NAME = "sigmoid_fitted";
    public static final int N_SAMPLES = 1000;

    public static void main(String[] args) {
        var parameters = FittingParametersFactory.produceDefault();
        double range= parameters.range();
        double margin= parameters.margin();
        TrainDataInOut data = getTrainData(range);
        var fitter = FitterFunctionFactory.produceOutput(parameters);
        IntStream.range(0, N_EPOCHS).forEach(i -> fitter.fit(data));
        var xList= createFromStartToEndWithNofItems(-range,range+margin, N_POINTS);
        var yList = fitter.read(xList);
        var chart = ChartFactory.getChart(xList, yList);
        ChartUtility.reduceXAxisTicksClutter(chart,2, "0");
        showChartSaveInFolderConcepts(chart, FILE_NAME);
    }


    private static TrainDataInOut getTrainData(double range) {
        var data = TrainDataInOut.empty();
        for (int i = 0; i < N_SAMPLES; i++) {
            double xRand= RandUtils.doubleInInterval(-range, range);
            double y= SigmoidFunctions.sigmoid.applyAsDouble(xRand);
            data.add(List.of(xRand), y);
        }
        return data;
    }


}
