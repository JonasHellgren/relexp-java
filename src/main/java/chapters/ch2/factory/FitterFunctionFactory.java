package chapters.ch2.factory;

import chapters.ch2.domain.fitting.FittingParameters;
import chapters.ch2.domain.fitting.MemoryFitterOutput;
import chapters.ch2.impl.function_fitting.*;
import com.google.common.collect.Range;
import core.foundation.gadget.math.BucketFinder;
import lombok.experimental.UtilityClass;


/***
 * MemoryFitter factory
 */

@UtilityClass
public class FitterFunctionFactory {

    public static FitterFunctionOutput produceOutput(FittingParameters parameters) {
        MemoryFitterOutput fitter = MemoryFitterOutput.of(getFinder(parameters), parameters);
        return FitterFunctionOutput.of(fitter);
    }

    private static BucketFinder getFinder(FittingParameters parameters) {
        double range= parameters.range();
        double dx= parameters.deltaX();
        return BucketFinder.of(Range.closed(-range, range), dx);  //closed <=> inclusive
    }

}
