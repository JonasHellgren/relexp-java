package chapters.ch8.plotting;

import chapters.ch8.domain.trainer.core.TrainerParking;
import core.foundation.config.ProjectPropertiesReader;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.util.List;

@UtilityClass
public class TrainerPlotter {

    static final int N_WINDOWS_FILTERING = 10;

    public static void plotTrainEvolution(TrainerParking trainer) {
        plotMeasure(trainer, "parking_training_rAvg_", MeasuresParkingTrainingEnum.REWARD_AVG);
        plotMeasure(trainer, "parking_training_rAvg_", MeasuresParkingTrainingEnum.NOCCUP_AVG);
    }

    @SneakyThrows
    private static void plotMeasure(TrainerParking trainer,
                                   String fileName,
                                   MeasuresParkingTrainingEnum measuresPendulumTrainingEnum) {
        var recorder = trainer.getRecorder();
        var path = ProjectPropertiesReader.create().pathNonEpisodic();
        System.out.println("path = " + path);
        var plotter = ErrorBandPlotterParking.ofFiltering(
                recorder, path, fileName, N_WINDOWS_FILTERING);
        plotter.plotAndSave(List.of(measuresPendulumTrainingEnum));
    }


}
