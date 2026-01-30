package ch7;

import chapters.ch7.domain.trainer.TrainerOneStepTdQLearningWithSafety;
import chapters.ch7.factory.SafetyLayerFactoryTreasure;
import chapters.ch7.factory.TrainerDependencySafeFactory;
import core.foundation.config.ConfigFactory;
import core.foundation.config.PathAndFile;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.List2ArrayConverterUtil;
import core.foundation.util.collections.ListUtil;
import core.plotting_core.plotting_2d.ErrorBandCreator;
import core.plotting_rl.progress_plotting.ErrorBandSaverAndPlotter;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import lombok.SneakyThrows;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RunnerHardCodedFailAvgReturnVsLearningRateAndProbRandom {

    public static final int N_EPISODES = 10_000;
    public static final int N_SAMPLES = 100;
    static final List<Double> LEARNING_RATES = List.of(0.01,0.1,0.5);
    static final List<Double> PROB_STARTS = List.of(0.5,0.6,0.7, 0.8, 0.9,1.0);
    public static final String Y_LABEL = "Average return";
    public static final String X_LABEL = "Initial probability of random action";
    public static final String FILE_NAME = "AvgReturnVsLearningRateAndProbRandomTreasureSafe";
    public static final boolean SHOW_LEGEND = true;
    public static final boolean SHOW_MARKER = true;

    @SneakyThrows
    public static void main(String[] args) {
        var plotConfig= ConfigFactory.plotConfig();
        var plotSettings = ErrorBandSaverAndPlotter.getSettings(
                Y_LABEL, X_LABEL, SHOW_LEGEND, SHOW_MARKER,plotConfig);
        var creator = ErrorBandCreator.newOfSettings(plotSettings);
        LEARNING_RATES.forEach(learningRate -> createCurveForLearningRate(learningRate, creator));
        String path = ProjectPropertiesReader.create().pathSafe();
        ErrorBandSaverAndPlotter.showAndSave(creator, PathAndFile.ofPng(path, FILE_NAME));
    }

    private static void createCurveForLearningRate(double lr, ErrorBandCreator creator) {
        List<Double> meanValues = new ArrayList<>();
        List<Double> standardDeviations = new ArrayList<>();
        for (double probability : PROB_STARTS) {
            var statistics = getStatistics(lr, probability);
            meanValues.add(statistics.getMean());
            standardDeviations.add(statistics.getStandardDeviation());
        }
        creator.addErrorBand(Double.toString(lr),
                List2ArrayConverterUtil.convertListToDoubleArr(PROB_STARTS),
                List2ArrayConverterUtil.convertListToDoubleArr(meanValues),
                List2ArrayConverterUtil.convertListToDoubleArr(standardDeviations),
                Color.BLACK);
    }
    private static DescriptiveStatistics getStatistics(double learningRate, double probability) {
        DescriptiveStatistics statistics = new DescriptiveStatistics();
        IntStream.range(0, N_SAMPLES)
                .mapToDouble(sampleIndex -> getAverageReturn(learningRate, probability))
                .forEach(statistics::addValue);
        return statistics;
    }

    private static double getAverageReturn(double learningRateStart, double probRandStart) {
        var dependencies = TrainerDependencySafeFactory.treasure(N_EPISODES, learningRateStart, probRandStart);
        var safetyLayer = SafetyLayerFactoryTreasure.produce(dependencies);
        var trainer = TrainerOneStepTdQLearningWithSafety.givenSafetyLayerOf(dependencies, safetyLayer);
        trainer.train();
        var returnList = trainer.getRecorder().trajectory(ProgressMeasureEnum.RETURN);
        return ListUtil.findAverage(returnList).orElseThrow();
    }

}

