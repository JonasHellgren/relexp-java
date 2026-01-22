package chapters.ch4.plotting;

import core.foundation.config.PlotConfig;
import core.gridrl.TrainerGridDependencies;
import core.plotting_rl.chart.GridAgentPlotterOld;
import core.plotting_rl.progress_plotting.PlotterProgressMeasures;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class GridPlotShowAndSave {


    @SneakyThrows
    public static void showAndSavePlots(TrainerGridDependencies dependencies,
                                        RecorderProgressMeasures recorder,
                                        String fileNameAddOns,
                                        int nofDigits,
                                        String picPath,
                                        PlotConfig plotCfg) {
        var agentPlotter= GridAgentPlotterOld.of(dependencies, fileNameAddOns, nofDigits,plotCfg);
        agentPlotter.plotAndSaveStateValuesInFolderTempDiff();
        agentPlotter.plotAndSavePolicyInFolderTempDiff();
        var progressPlotter = PlotterProgressMeasures.of(recorder, picPath, fileNameAddOns);
        progressPlotter.plotAndSave(List.of(ProgressMeasureEnum.RETURN, ProgressMeasureEnum.TD_ERROR));
    }
}
