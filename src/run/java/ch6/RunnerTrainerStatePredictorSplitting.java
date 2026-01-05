package ch6;

import chapters.ch6._shared.plotting.PlottingFactoryMultiStep;
import chapters.ch6.domain.trainer.core.TrainerStatePredictor;
import chapters.ch6.implem.factory.TrainerDependenciesFactory;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.List2ArrayConverter;
import core.gridrl.StateGrid;
import core.plotting.plotting_2d.ErrorBandCreator;
import lombok.SneakyThrows;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunnerTrainerStatePredictorSplitting {

    public static final int N_EPISODES = 200;
    public static final double TOL = 0.3;
    public static final double LEARNING_RATE_START = 0.1;
    public static final List<Double> HORIZONS = List.of(1d, 2d, 3d, 4d, 5d);
    public static final int N_SAMPLES = 50;

    @SneakyThrows
    public static void main(String[] args) {
        List<Double> meanList = new ArrayList<>();
        List<Double> stdList = new ArrayList<>();
        fillMeanAndStdLis(meanList, stdList);
        plotting(meanList, stdList);

    }

    private static void fillMeanAndStdLis(List<Double> meanList, List<Double> stdList) {
        for (double horizon:HORIZONS) {
             var ds=new DescriptiveStatistics();
            for (int j = 0; j < N_SAMPLES; j++) {
                var trainer = defineTrainer((int) horizon);
                trainer.train();
                var agent= trainer.getDependencies().agent();
                double value01= agent.read(StateGrid.of(0,1));
                ds.addValue(value01);
            }
            meanList.add(ds.getMean());
            stdList.add(ds.getStandardDeviation());
        }
    }

    private static void plotting(List<Double> meanList, List<Double> stdList) throws IOException {
        var settings= PlottingFactoryMultiStep.getFrameSettings("Value state (x,y)=(0,1)","n");
        var creator = ErrorBandCreator.newOfSettings(settings);
        creator.addErrorBand(
                "1",
                List2ArrayConverter.convertListToDoubleArr(HORIZONS),
                List2ArrayConverter.convertListToDoubleArr(meanList),
                List2ArrayConverter.convertListToDoubleArr(stdList), Color.BLACK);

        var path = ProjectPropertiesReader.create().pathMultiStep();
        creator.saveAsPicture(path+"TrainerStatePredictor.png");
        SwingUtilities.invokeLater(() -> creator.createFrame().setVisible(true));
    }


    private static TrainerStatePredictor defineTrainerOpt(int nStepsHorizon) {
        var dependencies = TrainerDependenciesFactory.givenOptimalPolicySplitting(nStepsHorizon, N_EPISODES, LEARNING_RATE_START);
        return TrainerStatePredictor.of(dependencies);
    }

    private static TrainerStatePredictor defineTrainer(int nStepsHorizon) {
        var dependencies = TrainerDependenciesFactory.givenRandomPolicySplitting(nStepsHorizon, N_EPISODES, LEARNING_RATE_START);
        return TrainerStatePredictor.of(dependencies);
    }


}
