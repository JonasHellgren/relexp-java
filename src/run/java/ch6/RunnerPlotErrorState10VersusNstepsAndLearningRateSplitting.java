package ch6;

import chapters.ch6.domain.trainer.core.TrainerStateActionControlDuringEpisode;
import chapters.ch6.implem.factory.TrainerDependenciesFactory;
import core.foundation.configOld.PathAndFile;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.List2ArrayConverter;
import core.foundation.util.collections.MyListUtils;
import core.plotting.plotting_2d.ErrorBandCreator;
import core.plotting_rl.progress_plotting.ErrorBandSaverAndPlotter;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import lombok.SneakyThrows;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunnerPlotErrorState10VersusNstepsAndLearningRateSplitting {

    public static final int N_EPISODES = 100;
    static final List<Double> LEARNING_RATES = List.of(0.001,0.01,0.1);
    static final List<Integer> N_STEPS = List.of(1,2, 3,4,5,6);
    public static final int N_SAMPLES = 100;
    public static final boolean SHOW_LEGEND = true;
    public static final boolean SHOW_MARKER = true;
    public static final String FILE_NAME = "AvgReturnVersusNstepsAndLearningRateSplitting";

    @SneakyThrows
    public static void main(String[] args) {
        var plotSettings = ErrorBandSaverAndPlotter.getSettings("Average return", "n", SHOW_LEGEND, SHOW_MARKER);
        var creator = ErrorBandCreator.newOfSettings(plotSettings);
        for (double lr : LEARNING_RATES) {
            List<Double> meanList = new ArrayList<>();
            List<Double> stdList = new ArrayList<>();
            fillMeanAndStdList(lr, meanList, stdList);
            addDataToPlotCreator(lr, creator, meanList, stdList);
        }
        plotting(creator);
    }

    private static void plotting(ErrorBandCreator creator) throws IOException {
        String path= ProjectPropertiesReader.create().pathMultiStep();
        ErrorBandSaverAndPlotter.showAndSave(creator, PathAndFile.ofPng(path, FILE_NAME));
    }

    private static void addDataToPlotCreator(double lr, ErrorBandCreator creator, List<Double> meanList, List<Double> stdList) {
        creator.addErrorBand(Double.toString(lr),
                List2ArrayConverter.convertIntegerListToDoubleArr(N_STEPS),
                List2ArrayConverter.convertListToDoubleArr(meanList),
                List2ArrayConverter.convertListToDoubleArr(stdList),
                Color.BLACK);
    }

    private static void fillMeanAndStdList(double lr, List<Double> meanList, List<Double> stdList) {
        for (int nSteps : N_STEPS) {
            var ds = new DescriptiveStatistics();
            for (int i = 0; i < N_SAMPLES; i++) {
                var trainer = defineTrainer(nSteps, lr);
                trainer.train();
                var recorder = trainer.getRecorder();
                var returns = recorder.trajectory(ProgressMeasureEnum.RETURN);
                double meanAvg = MyListUtils.findAverage(returns).orElseThrow();
                ds.addValue(meanAvg);
            }
            meanList.add(ds.getMean());
            stdList.add(ds.getStandardDeviation());
        }
    }


    private static TrainerStateActionControlDuringEpisode defineTrainer(int nStepsHorizon, double learningRateStart) {
        var dependencies = TrainerDependenciesFactory.learnPolicySplittingDuringEpis(nStepsHorizon, N_EPISODES, learningRateStart);
        return TrainerStateActionControlDuringEpisode.of(dependencies);
    }


}
