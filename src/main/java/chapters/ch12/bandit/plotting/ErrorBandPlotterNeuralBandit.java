package chapters.ch12.bandit.plotting;

import com.google.common.base.Preconditions;
import core.foundation.config.PathAndFile;
import core.plotting.plotting_2d.ErrorBandCreator;
import core.plotting.progress_plotting.ErrorBandData;
import core.plotting.progress_plotting.ErrorBandSaverAndPlotter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.awt.*;
import java.util.List;

/**
 * A utility class for plotting cannon training process.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorBandPlotterNeuralBandit {

    public static final String X_LABEL = "Episode";
    private final BanditNeuralRecorder recorder;
    private final String filePath;
    private final String fileNameAddOn;
    private final int nWindows;

    public static ErrorBandPlotterNeuralBandit ofFiltering(BanditNeuralRecorder recorder,
                                                           String filePath,
                                                           String fileNameAddOn,
                                                           int nWindows) {
        return new ErrorBandPlotterNeuralBandit(recorder, filePath, fileNameAddOn,nWindows);
    }

    public void plotAndSave(List<MeasuresBanditNeuralEnum> measures) {
        Preconditions.checkArgument(!recorder.isEmpty(), "No training progress data to plot");
        measures.forEach(measure ->
                showAndSavePlot(measure, ErrorBandData.of(recorder.trajectory(measure), nWindows)));
    }

    private void showAndSavePlot(MeasuresBanditNeuralEnum measure, ErrorBandData errorBandData) {
        var settings= ErrorBandSaverAndPlotter.getSettings(measure.description, X_LABEL, false, false);
        var creator = ErrorBandCreator.newOfSettings(settings);
        addErrorBandFilter(measure, creator, errorBandData);
        ErrorBandSaverAndPlotter.showAndSave(creator, PathAndFile.ofPng(filePath, measure+fileNameAddOn));
    }

    private static void addErrorBandFilter(MeasuresBanditNeuralEnum measure, ErrorBandCreator creator, ErrorBandData errorBandData) {
        creator.addErrorBand(measure.description,
                errorBandData.xDataAsArray(),
                errorBandData.yDataFilteredAsArray(),
                errorBandData.errDataFilteredAsArray(),
                Color.BLACK);
    }

}
