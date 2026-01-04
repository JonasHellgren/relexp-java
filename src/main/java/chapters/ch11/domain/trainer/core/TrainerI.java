package chapters.ch11.domain.trainer.core;


import core.plotting.progress_plotting.RecorderProgressMeasures;

public interface TrainerI {
    RecorderProgressMeasures getRecorder();
    TrainerDependencies getDependencies();
    void train();
}
