package chapters.ch4.plotting;

import core.foundation.config.PlotConfig;
import core.gridrl.TrainerGridDependencies;
import core.plotting_rl.chart.GridAgentPlotter;
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
                                        String fileName,
                                        int nofDigits,
                                        String picPath,
                                        PlotConfig plotCfg) {
        var agentPlotter = GridAgentPlotter.of(dependencies, nofDigits,plotCfg,picPath);
        agentPlotter.saveAndPlotPolicy(fileName+"_policy");
        agentPlotter.saveAndPlotStateValues(fileName+"_values");

        var progressPlotter = PlotterProgressMeasures.of(recorder, picPath, fileName);
        progressPlotter.plotAndSave(List.of(ProgressMeasureEnum.RETURN, ProgressMeasureEnum.TD_ERROR));
    }
}
