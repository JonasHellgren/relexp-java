package chapters.ch11.domain.trainer.core;


import core.plotting_rl.progress_plotting.RecorderProgressMeasures;

public interface TrainerI {
    RecorderProgressMeasures getRecorder();
    TrainerDependencies getDependencies();
    void train();
}
