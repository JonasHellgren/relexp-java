package chapters.ch2.domain;


import core.foundation.gadget.training.TrainDataInOut;

public interface MemoryFitterI {
    void fit(TrainDataInOut data);
    TabularMemory getMemory();
    double read(double in);
}
