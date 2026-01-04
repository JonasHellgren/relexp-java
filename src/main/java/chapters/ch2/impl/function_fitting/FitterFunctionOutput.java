package chapters.ch2.impl.function_fitting;

import chapters.ch2.domain.fitting.FitterFunctionI;
import chapters.ch2.domain.fitting.MemoryFitterOutput;
import chapters.ch2.domain.fitting.TabularMemory;
import core.foundation.gadget.training.TrainDataInOut;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FitterFunctionOutput implements FitterFunctionI {
    private final MemoryFitterOutput fitter;
    FitterOutCalculator outCalculator;

    public static FitterFunctionOutput of(MemoryFitterOutput fitter) {
        return new FitterFunctionOutput(fitter, FitterOutCalculator.of(fitter));
    }


    public TabularMemory getMemory() {
        return fitter.getMemory();
    }

    @Override
    public void fit(TrainDataInOut data) {
        fitter.fit(data);
    }

    @Override
    public double read(double in) {
        return fitter.read(in);
    }

    @Override
    public List<Double> read(List<Double> in) {
        return outCalculator.produceOutput(in, fitter.getParameters());
    }

}
