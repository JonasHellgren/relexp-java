package chapters.ch6.domain.trainer.core;

/**
 * This interface represents a trainer in the multi-step domain.
 * It provides methods to start the training process and access the trainer's dependencies.
 */
public interface TrainerI {
    void train();
    TrainerDependenciesMultiStep getDependencies();
}
