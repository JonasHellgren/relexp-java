package chapters.ch2.domain.fitting;


import core.foundation.gadget.training.TrainDataInOut;

public interface MemoryFitterI {
    void fit(TrainDataInOut data);
    TabularMemory getMemory();
    double read(double in);
}
