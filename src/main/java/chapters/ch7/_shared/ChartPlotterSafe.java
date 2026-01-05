package chapters.ch7._shared;

import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting.chart_plotting.GridAgentPlotter;
import core.plotting.progress_plotting.PlotterProgressMeasures;
import core.plotting.progress_plotting.ProgressMeasureEnum;
import core.plotting.progress_plotting.RecorderProgressMeasures;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.util.List;

/**
 * A utility class for plotting and saving charts related to safe learning.
 */
@UtilityClass
public class ChartPlotterSafe {

    @SneakyThrows
    public static void showAndSavePlots(TrainerGridDependencies dependencies,
                                        RecorderProgressMeasures recorder,
                                        String fileNameAddOns,
                                        int nofDigits) {
        var agentPlotter = GridAgentPlotter.of(dependencies, fileNameAddOns, nofDigits);
        agentPlotter.plotStateValuesInFolderSafe();
        agentPlotter.plotPolicyInFolderSafe();
        var path = ProjectPropertiesReader.create().pathSafe();
        var progressPlotter = PlotterProgressMeasures.of(recorder, path, fileNameAddOns);
        progressPlotter.plotAndSave(List.of(ProgressMeasureEnum.RETURN, ProgressMeasureEnum.N_STEPS));
        progressPlotter.plotAndSaveNoFiltering(List.of(ProgressMeasureEnum.SIZE_MEMORY));
    }

}
