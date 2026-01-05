package chapters.ch4.plotting;

import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting.chart_plotting.GridAgentPlotter;
import core.plotting.progress_plotting.PlotterProgressMeasures;
import core.plotting.progress_plotting.ProgressMeasureEnum;
import core.plotting.progress_plotting.RecorderProgressMeasures;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class GridPlotHelper {


    @SneakyThrows
    public static void showAndSavePlots(TrainerGridDependencies dependencies,
                                        RecorderProgressMeasures recorder,
                                        String fileNameAddOns, int nofDigits) {
        var agentPlotter= GridAgentPlotter.of(dependencies, fileNameAddOns, nofDigits);
        agentPlotter.plotAndSaveStateValuesInFolderTempDiff();
        agentPlotter.plotAndSavePolicyInFolderTempDiff();
        var path= ProjectPropertiesReader.create().pathTempDiff();
        var progressPlotter = PlotterProgressMeasures.of(recorder, path, fileNameAddOns);
        progressPlotter.plotAndSave(List.of(ProgressMeasureEnum.RETURN, ProgressMeasureEnum.TD_ERROR));
    }
}
