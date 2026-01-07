package core.plotting_rl.progress_plotting;

import com.google.common.base.Preconditions;
import core.foundation.configOld.PathAndFile;
import core.plotting.base.shared.PlotSettings;
import core.plotting.plotting_2d.ErrorBandCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.awt.*;
import java.util.List;

/**
 * A utility class for plotting progress measures of a training process.
 */

@Log
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlotterProgressMeasures {

    public static final int N_WINDOWS = 10;  //for much filtering
    public static final int MANY_WINDOWS = 100; //for little filtering
    public static final String EPISODE = "Episode";
    public static final int SIZE = 34, SIZE_TICKS=28;

    private final RecorderProgressMeasures recorder;
    private final String filePath;
    private final String fileNameAddOn;


    public static PlotterProgressMeasures of(RecorderProgressMeasures recorder, String file, String fileNameAddOn) {
        return new PlotterProgressMeasures(recorder, file, fileNameAddOn);
    }

    public static PlotterProgressMeasures of(RecorderProgressMeasures recorder, String file) {
        return new PlotterProgressMeasures(recorder, file, "");
    }

    public void plotAndSave(List<ProgressMeasureEnum> measures) {
        Preconditions.checkArgument(!recorder.isEmpty(), "No training progress data to plot");
        for (var measure : measures) {
            var errorBandData = ErrorBandData.of(recorder.trajectory(measure), N_WINDOWS);
            showAndSavePlot(measure, errorBandData);
        }
    }

    public void plotAndSaveNoFiltering(List<ProgressMeasureEnum> measures) {
        Preconditions.checkArgument(!recorder.isEmpty(), "No training progress data to plot");
        for (var measure : measures) {
            var errorBandData = ErrorBandData.of(recorder.trajectory(measure), MANY_WINDOWS);
            showAndSavePlot(measure, errorBandData);
        }
    }

    private void showAndSavePlot(ProgressMeasureEnum measure, ErrorBandData errorBandData) {
        var creator = ErrorBandCreator.newOfSettings(getSettingsNoLegendNoMarker(measure.getDescription(), EPISODE));
        addErrorBand(measure, creator, errorBandData);
        showAndSave(creator, filePath, measure, fileNameAddOn);
    }

    private static void addErrorBand(ProgressMeasureEnum measure, ErrorBandCreator creator, ErrorBandData errorBandData) {
        creator.addErrorBand(measure.getDescription(),
                errorBandData.xDataAsArray(),
                errorBandData.yDataFilteredAsArray(),
                errorBandData.errDataFilteredAsArray(),
                Color.BLACK);
    }
    private void showAndSave(ErrorBandCreator creator, String pathPics, ProgressMeasureEnum measure, String fileNameAddOn) {
        ErrorBandSaverAndPlotter.showAndSave(creator, PathAndFile.ofPng(pathPics, measure+fileNameAddOn));
    }

    private static PlotSettings getSettingsNoLegendNoMarker(String yLabel, String xLabel) {
        var plotSettings = ErrorBandSaverAndPlotter.getSettings(yLabel, xLabel, false, false);
        plotSettings=plotSettings
                .withAxisTicksFont(new Font("Arial", Font.PLAIN, SIZE_TICKS))
                .withAxisTitleFont(new Font("Arial", Font.BOLD, SIZE))
                .withLegendTextFont(new Font("Arial", Font.BOLD, SIZE));
        return plotSettings;
    }

}
