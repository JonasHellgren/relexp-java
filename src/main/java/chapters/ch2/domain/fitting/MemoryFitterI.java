package chapters.ch2.domain.fitting;


import core.foundation.gadget.training.TrainData;

public interface MemoryFitterI {
    void fit(TrainData data);
    TabularMemory getMemory();
    double read(double in);
}
