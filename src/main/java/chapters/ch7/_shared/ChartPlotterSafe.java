package chapters.ch7._shared;

import core.foundation.config.ConfigFactory;
import core.gridrl.TrainerGridDependencies;
import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting_rl.chart.GridAgentPlotter;
import core.plotting_rl.progress_plotting.PlotterProgressMeasures;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
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
        var plotCfg= ConfigFactory.plotConfig();
        var agentPlotter = GridAgentPlotter.of(dependencies, fileNameAddOns, nofDigits,plotCfg);
        agentPlotter.plotStateValuesInFolderSafe();
        agentPlotter.plotPolicyInFolderSafe();
        var path = ProjectPropertiesReader.create().pathSafe();
        var progressPlotter = PlotterProgressMeasures.of(recorder, path, fileNameAddOns);
        progressPlotter.plotAndSave(List.of(ProgressMeasureEnum.RETURN, ProgressMeasureEnum.N_STEPS));
        progressPlotter.plotAndSaveNoFiltering(List.of(ProgressMeasureEnum.SIZE_MEMORY));
    }

}
