package chapters.ch11.plotting;

import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.core.TrainerLunarMultiStep;
import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting_rl.progress_plotting.PlotterProgressMeasures;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import lombok.SneakyThrows;

import java.util.List;

public class LunarTrainerPlotter {

    @SneakyThrows
    public static void plot(TrainerLunarMultiStep trainer, TrainerDependencies trainerDependencies) {
        var pathPics = ProjectPropertiesReader.create().pathActorCriticPics();
        var progressPlotter = PlotterProgressMeasures.of(trainer.getRecorder(), pathPics);
        progressPlotter.plotAndSave(
                List.of(ProgressMeasureEnum.RETURN,
                        ProgressMeasureEnum.STD_ACTOR,
                        ProgressMeasureEnum.TD_BEST_ACTION,
                        ProgressMeasureEnum.GRADIENT_ACTOR_MEAN));
        var agentPlotter = PlotterHeatMapsAgent.of(trainerDependencies);
        agentPlotter.plotAndSaveAll();
    }

}
