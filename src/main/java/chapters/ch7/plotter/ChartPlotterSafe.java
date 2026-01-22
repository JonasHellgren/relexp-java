package chapters.ch7.plotter;

import chapters.ch7.domain.trainer.TrainerOneStepTdQLearningWithSafety;
import core.foundation.config.PlotConfig;
import core.gridrl.TrainerGridDependencies;
import core.plotting_rl.chart.GridAgentPlotter;
import core.plotting_rl.progress_plotting.PlotterProgressMeasures;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
import lombok.experimental.UtilityClass;
import java.util.List;

/**
 * A utility class for plotting and saving charts related to safe learning.
 */
@UtilityClass
public class ChartPlotterSafe {

    public static final int NOF_DIGITS = 1;

    public static void showAndSavePlots(TrainerOneStepTdQLearningWithSafety trainer,
                                        String fileName, PlotConfig plotCfg, String path) {
        showAndSavePlots(trainer.getDependencies(), trainer.getRecorder(), fileName, plotCfg, path);
    }


    public static void showAndSavePlots(TrainerGridDependencies dependencies,
                                        RecorderProgressMeasures recorder,
                                        String fileName, PlotConfig plotCfg, String path) {
        var agentPlotter = GridAgentPlotter.of(dependencies, NOF_DIGITS,plotCfg, path);
        agentPlotter.saveAndPlotStateValues(fileName+"values");
        agentPlotter.saveAndPlotPolicy(fileName+"policy");
        var progressPlotter = PlotterProgressMeasures.of(recorder, path, fileName);
        progressPlotter.plotAndSave(List.of(ProgressMeasureEnum.RETURN, ProgressMeasureEnum.N_STEPS));
        progressPlotter.plotAndSaveNoFiltering(List.of(ProgressMeasureEnum.SIZE_MEMORY));

    }

}
