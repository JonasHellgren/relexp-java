package chapters.ch2.implem.parameter_fitting;

import chapters.ch2.implem.function_fitting.FittingParameters;
import chapters.ch2.implem.function_fitting.MemoryFitterOutput;
import com.google.common.collect.Range;
import core.foundation.gadget.math.BucketFinder;
import core.foundation.gadget.training.TrainData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Used by FitterSingleParameter
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FitterSingleParameter {
    private final MemoryFitterOutput fitter;

    public static FitterSingleParameter of(FittingParameters parameters) {
        double inf = Double.POSITIVE_INFINITY;
        var finder = BucketFinder.of(Range.openClosed(-inf, inf), 1);
        var fitter= MemoryFitterOutput.of(finder, parameters);
        return new FitterSingleParameter(fitter);
    }

    public void fit(TrainData data) {
        fitter.fit(data);
    }


    public double read(double in) {
        return fitter.read(in);
    }
}
