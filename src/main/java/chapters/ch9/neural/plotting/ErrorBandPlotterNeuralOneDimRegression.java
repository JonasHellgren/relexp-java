package chapters.ch9.neural.plotting;

import com.google.common.base.Preconditions;
import core.foundation.config.PathAndFile;
import core.foundation.util.collections.ArrayCreatorUtil;
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
public class ErrorBandPlotterNeuralOneDimRegression {

    public static final String X_LABEL = "Iteration";
    private final NeuralOneDimRegressionRecorder recorder;
    private final String filePath;
    private final String fileNameAddOn;
    private final int nWindows;

    public static ErrorBandPlotterNeuralOneDimRegression ofFiltering(NeuralOneDimRegressionRecorder recorder,
                                                                     String filePath,
                                                                     String fileNameAddOn,
                                                                     int nWindows) {
        return new ErrorBandPlotterNeuralOneDimRegression(recorder, filePath, fileNameAddOn,nWindows);
    }

    public void plotAndSave(List<MeasuresOneDimRegressionNeuralEnum> measures) {
        Preconditions.checkArgument(!recorder.isEmpty(), "No training progress data to plot");
        for (var measure : measures) {
            var errorBandData = ErrorBandData.of(recorder.trajectory(measure), nWindows);
            showAndSavePlot(measure, errorBandData);
        }
    }

    private void showAndSavePlot(MeasuresOneDimRegressionNeuralEnum measure, ErrorBandData errorBandData) {
        var settings= ErrorBandSaverAndPlotter.getSettings(measure.description, X_LABEL, false, false);
        var creator = ErrorBandCreator.newOfSettings(settings);
        addErrorBandFilter(measure, creator, errorBandData);
        ErrorBandSaverAndPlotter.showAndSave(creator, PathAndFile.ofPng(filePath, measure+fileNameAddOn));
    }

    private static void addErrorBandFilter(MeasuresOneDimRegressionNeuralEnum measure, ErrorBandCreator creator, ErrorBandData errorBandData) {
        //double[] errData = errorBandData.errDataFilteredAsArray();
        double[] errData= ArrayCreatorUtil.createArrayWithSameDoubleNumber(errorBandData.xDataAsArray().length, 0.0);
        creator.addErrorBand(measure.description,
                errorBandData.xDataAsArray(),
                errorBandData.yDataFilteredAsArray(),
                errData,
                Color.BLACK);
    }

}
