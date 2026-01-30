package chapters.ch12.plotting_invpend;

import com.google.common.base.Preconditions;
import core.foundation.config.PathAndFile;
import core.plotting_core.plotting_2d.ErrorBandCreator;
import core.plotting_rl.progress_plotting.ErrorBandData;
import core.plotting_rl.progress_plotting.ErrorBandSaverAndPlotter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.awt.*;
import java.util.List;

/**
 * A utility class for plotting cannon training process.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorBandPlotterNeuralPendulum {

    public static final String X_LABEL = "Episode";
    private final RecorderTrainerPendulum recorder;
    private final String filePath;
    private final String fileNameAddOn;
    private final int nWindows;

    public static ErrorBandPlotterNeuralPendulum ofFiltering(RecorderTrainerPendulum recorder,
                                                             String filePath,
                                                             String fileNameAddOn,
                                                             int nWindows) {
        return new ErrorBandPlotterNeuralPendulum(recorder, filePath, fileNameAddOn,nWindows);
    }

    public void plotAndSave(List<MeasuresPendulumTrainingEnum> measures) {
        Preconditions.checkArgument(!recorder.isEmpty(), "No training progress data to plot");
        measures.forEach(measure ->
                showAndSavePlot(measure, ErrorBandData.of(recorder.trajectory(measure), nWindows)));
    }

    private void showAndSavePlot(MeasuresPendulumTrainingEnum measure, ErrorBandData errorBandData) {
        var settings= ErrorBandSaverAndPlotter.getSettings(measure.description, X_LABEL, false, false);
        var creator = ErrorBandCreator.newOfSettings(settings);
        addErrorBandFilter(measure, creator, errorBandData);
        ErrorBandSaverAndPlotter.showAndSave(creator, PathAndFile.ofPng(filePath, measure+fileNameAddOn));
    }

    private static void addErrorBandFilter(MeasuresPendulumTrainingEnum measure, ErrorBandCreator creator, ErrorBandData errorBandData) {
        creator.addErrorBand(measure.description,
                errorBandData.xDataAsArray(),
                errorBandData.yDataFilteredAsArray(),
                errorBandData.errDataFilteredAsArray(),
                Color.BLACK);
    }

}
