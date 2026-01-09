package ch2;

import chapters.ch2.factory.FittingParametersFactory;
import chapters.ch2.factory.ManyLinesChartCreatorFactory;
import chapters.ch2.factory.TrainingResultsGenerator;
import chapters.ch2.plotting.SingleParameterFittingPlotter;
import core.foundation.config.ConfigFactory;
import core.foundation.config.PlotConfig;
import lombok.SneakyThrows;

import java.util.List;

public class RunnerFitterSingleParameter {

    static final List<Double> LEARNING_RATES = List.of(0.1, 0.2, 0.4, 0.8);
    public static final int NOF_ITERATIONS = 51;
    public static final String FILE_NAME = "fitterSingleParameter";

    @SneakyThrows
    public static void main(String[] args) {
        var par = FittingParametersFactory.produceDefault()
                .withNofIterations(NOF_ITERATIONS);
        var plotCfg = ConfigFactory.plotConfig();
        var creator= ManyLinesChartCreatorFactory.produce(par.nofIterations(), plotCfg);
        var trainingResults = TrainingResultsGenerator.getTrainingResults(par, LEARNING_RATES);
        LEARNING_RATES.forEach(lr ->  SingleParameterFittingPlotter.addResultToCreator(trainingResults, creator,lr));
        SingleParameterFittingPlotter.saveAndPlot(creator, ConfigFactory.pathPicsConfig(),FILE_NAME);
    }

}
