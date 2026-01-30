package chapters.ch10.plotting;

import chapters.ch10.bandit.domain.trainer.RecorderBandit;
import com.google.common.base.Preconditions;
import core.foundation.config.ConfigFactory;
import core.foundation.config.PathAndFile;
import core.foundation.util.cond.ConditionalsUtil;
import core.plotting_core.plotting_2d.ErrorBandCreator;
import core.plotting_rl.progress_plotting.ErrorBandData;
import core.plotting_rl.progress_plotting.ErrorBandSaverAndPlotter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.awt.*;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorBandPlotterBandit {

    public static final String X_LABEL = "Episode";
    private final RecorderBandit recorder;
    private final String filePath;
    private final String fileNameAddOn;
    private final int nWindows;
    private final boolean isFiltering;

    public static ErrorBandPlotterBandit ofNoFiltering(RecorderBandit recorder,
                                                       String filePath,
                                                       String fileNameAddOn) {
        return new ErrorBandPlotterBandit(recorder, filePath, fileNameAddOn,1,false);
    }

    public static ErrorBandPlotterBandit ofFiltering(RecorderBandit recorder,
                                                     String filePath,
                                                     String fileNameAddOn,
                                                     int nWindows) {
        return new ErrorBandPlotterBandit(recorder, filePath, fileNameAddOn,nWindows,true);
    }

    public void plotAndSave(List<MeasuresBanditEnum> measures) {
        Preconditions.checkArgument(!recorder.isEmpty(), "No training progress data to plot");
        for (var measure : measures) {
            var errorBandData = ErrorBandData.of(recorder.trajectory(measure), nWindows);
            showAndSavePlot(measure, errorBandData);
        }
    }

    private void showAndSavePlot(MeasuresBanditEnum measure, ErrorBandData errorBandData) {
        var plotConfig= ConfigFactory.plotConfig();
        var settings= ErrorBandSaverAndPlotter.getSettings(measure.description, X_LABEL, false, false,plotConfig);
        var creator = ErrorBandCreator.newOfSettings(settings);
        ConditionalsUtil.executeOneOfTwo(isFiltering,
                () -> addErrorBandFilter(measure, creator, errorBandData),
                () -> addErrorBandNoFilter(measure, creator, errorBandData));
        ErrorBandSaverAndPlotter.showAndSave(creator, PathAndFile.ofPng(filePath, measure+fileNameAddOn));
    }

    private static void addErrorBandFilter(MeasuresBanditEnum measure, ErrorBandCreator creator, ErrorBandData errorBandData) {
        creator.addErrorBand(measure.description,
                errorBandData.xDataAsArray(),
                errorBandData.yDataFilteredAsArray(),
                errorBandData.errDataFilteredAsArray(),
                Color.BLACK);
    }

    private static void addErrorBandNoFilter(MeasuresBanditEnum measure, ErrorBandCreator creator, ErrorBandData errorBandData) {
        creator.addErrorBand(measure.description,
                errorBandData.xDataAsArray(),
                errorBandData.yDataNotFilteredAsArray(),
                errorBandData.zeroErrDataAsArray(),
                Color.BLACK);
    }

}
