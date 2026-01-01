package chapters.ch2.implem.function_fitting;


import core.foundation.gadget.training.TrainData;

public interface FitterFunctionI {
    void fit(TrainData data);
    double read(double in);
}
