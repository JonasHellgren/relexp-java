package chapters.ch14.domain.interfaces;


import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.TrainDataOld;

/**
 * This package contains the LongMemory interface and its implementations.
 * LongMemory is an interface for storing and retrieving values for a given state.
 * It provides methods to read, write, and fit the memory with training data.
 */
public interface LongMemory<SI> {
    double read(SI state);
    void write(SI state, double value);
    void fit(TrainData data, int nFits);
    void fit(TrainData data);
}
