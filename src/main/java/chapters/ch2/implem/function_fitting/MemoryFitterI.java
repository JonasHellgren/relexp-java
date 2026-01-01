package chapters.ch2.implem.function_fitting;


import core.foundation.gadget.training.TrainData;

public interface MemoryFitterI {
    void fit(TrainData data);
    TabularMemory getMemory();
    double read(double in);
}
