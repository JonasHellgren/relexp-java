package chapters.ch2.implem.function_fitting;

import com.google.common.base.Preconditions;
import core.foundation.gadget.training.TrainData;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FitterFunctionDifferences implements FitterFunctionI{

    private final MemoryFitterI fitter;

    public TabularMemory getMemory() {
        return fitter.getMemory();
    }

    @Override
    public void fit(TrainData data) {
        Preconditions.checkArgument(data.isErrors(), "Data must contain errors");
        fitter.fit(data);
    }

    @Override
    public double read(double in) {
        return fitter.read(in);
    }

}
