package chapters.ch2.domain.fitting;

import core.foundation.gadget.training.TrainData;

import java.util.List;

public interface FitterFunctionI {
    void fit(TrainData data);
    double read(double in);
    List<Double> read(List<Double> in);
}
