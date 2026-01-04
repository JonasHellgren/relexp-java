package chapters.ch2.domain;

import core.foundation.gadget.training.TrainData;

public interface FitterFunctionI {
    void fit(TrainData data);
    double read(double in);
}
