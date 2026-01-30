package chapters.ch11.plotting;

import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.core.TrainerLunarMultiStep;
import core.plotting_rl.progress_plotting.PlotterProgressMeasures;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.util.List;

@UtilityClass
public class LunarTrainerPlotter {

    @SneakyThrows
    public static void plot(TrainerLunarMultiStep trainer,
                            TrainerDependencies trainerDependencies,
                            String pathPics) {
        var progressPlotter = PlotterProgressMeasures.of(trainer.getRecorder(), pathPics);
        progressPlotter.plotAndSave(
                List.of(ProgressMeasureEnum.RETURN,
                        ProgressMeasureEnum.STD_ACTOR,
                        ProgressMeasureEnum.TD_BEST_ACTION,
                        ProgressMeasureEnum.GRADIENT_ACTOR_MEAN));
        var agentPlotter = PlotterHeatMapsAgent.of(trainerDependencies);
        agentPlotter.plotAndSaveAll(pathPics);
    }

}
