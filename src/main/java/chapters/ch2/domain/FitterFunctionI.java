package chapters.ch2.domain;

import core.foundation.gadget.training.TrainDataInOut;

import java.util.List;

public interface FitterFunctionI {
    void fit(TrainDataInOut data);
    double read(double in);
    List<Double> read(List<Double> in);
}
