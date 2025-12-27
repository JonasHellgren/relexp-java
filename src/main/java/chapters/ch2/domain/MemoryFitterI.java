package chapters.ch2.domain;


import core.foundation.gadget.training.TrainData;

public interface MemoryFitterI {
    void fit(TrainData data);
    TabularMemory getMemory();
    double read(double in);
}
