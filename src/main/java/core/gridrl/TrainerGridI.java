package core.gridrl;

import core.plotting.progress_plotting.RecorderProgressMeasures;

/**
 * This interface represents a trainer for a grid world domain.
 * It provides methods to access the recorder and dependencies, as well as to start the training process.
 */
public interface TrainerGridI {
    RecorderProgressMeasures getRecorder();
    TrainerGridDependencies getDependencies();

    /**
     * Starts the training process.
     */
    void train();
}
