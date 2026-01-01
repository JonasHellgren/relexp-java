package chapters.ch2.domain;


import chapters.ch2.implem.function_fitting.TabularMemory;
import core.foundation.gadget.training.TrainData;

public interface MemoryFitterI {
    void fit(TrainData data);
    TabularMemory getMemory();
    double read(double in);
}
