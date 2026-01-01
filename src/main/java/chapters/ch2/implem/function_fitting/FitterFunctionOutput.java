package chapters.ch2.implem.function_fitting;

import chapters.ch2.domain.MemoryFitterOutput;
import com.google.common.base.Preconditions;
import core.foundation.gadget.training.TrainData;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FitterFunctionOutput implements FitterFunctionI{
    private final MemoryFitterOutput fitter;

    public TabularMemory getMemory() {
        return fitter.getMemory();
    }

    @Override
    public void fit(TrainData data) {
        Preconditions.checkArgument(data.isOutput(), "Data must contain outputs");
        fitter.fit(data);
    }

    @Override
    public double read(double in) {
        return fitter.read(in);
    }

}
