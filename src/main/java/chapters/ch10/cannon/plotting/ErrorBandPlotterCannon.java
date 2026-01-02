package chapters.ch10.cannon.plotting;

import chapters.ch10.cannon.domain.trainer.RecorderCannon;
import com.google.common.base.Preconditions;
import core.foundation.config.PathAndFile;
import core.foundation.util.cond.Conditionals;
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
public class ErrorBandPlotterCannon {

    public static final String X_LABEL = "Episode";
    private final RecorderCannon recorder;
    private final String filePath;
    private final String fileNameAddOn;
    private final int nWindows;
    private final boolean isFiltering;

    public static ErrorBandPlotterCannon ofNoFiltering(RecorderCannon recorder,
                                                       String filePath,
                                                       String fileNameAddOn) {
        return new ErrorBandPlotterCannon(recorder, filePath, fileNameAddOn,1,false);
    }

    public static ErrorBandPlotterCannon ofFiltering(RecorderCannon recorder,
                                                     String filePath,
                                                     String fileNameAddOn,
                                                     int nWindows) {
        return new ErrorBandPlotterCannon(recorder, filePath, fileNameAddOn,nWindows,true);
    }

    public void plotAndSave(List<MeasuresCannonEnum> measures) {
        Preconditions.checkArgument(!recorder.isEmpty(), "No training progress data to plot");
        for (var measure : measures) {
            var errorBandData = ErrorBandData.of(recorder.trajectory(measure), nWindows);
            showAndSavePlot(measure, errorBandData);
        }
    }

    private void showAndSavePlot(MeasuresCannonEnum measure, ErrorBandData errorBandData) {
        var settings= ErrorBandSaverAndPlotter.getSettings(measure.description, X_LABEL, false, false);
        var creator = ErrorBandCreator.newOfSettings(settings);
        Conditionals.executeOneOfTwo(isFiltering,
                () -> addErrorBandFilter(measure, creator, errorBandData),
                () -> addErrorBandNoFilter(measure, creator, errorBandData));
        ErrorBandSaverAndPlotter.showAndSave(creator, PathAndFile.ofPng(filePath, measure+fileNameAddOn));
    }

    private static void addErrorBandFilter(MeasuresCannonEnum measure, ErrorBandCreator creator, ErrorBandData errorBandData) {
        creator.addErrorBand(measure.description,
                errorBandData.xDataAsArray(),
                errorBandData.yDataFilteredAsArray(),
                errorBandData.errDataFilteredAsArray(),
                Color.BLACK);
    }

    private static void addErrorBandNoFilter(MeasuresCannonEnum measure, ErrorBandCreator creator, ErrorBandData errorBandData) {
        creator.addErrorBand(measure.description,
                errorBandData.xDataAsArray(),
                errorBandData.yDataNotFilteredAsArray(),
                errorBandData.zeroErrDataAsArray(),
                Color.BLACK);
    }

}
