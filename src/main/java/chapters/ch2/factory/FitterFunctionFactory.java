package chapters.ch2.factory;

import chapters.ch2.domain.MemoryFitterOutput;
import chapters.ch2.implem.function_fitting.*;
import com.google.common.collect.Range;
import core.foundation.gadget.math.BucketFinder;
import lombok.experimental.UtilityClass;


/***
 * MemoryFitter factory
 */

@UtilityClass
public class FitterFunctionFactory {

   public static FitterFunctionDifferences produceDifferences(FittingParameters parameters) {
        var fitter = MemoryFitterErrors.of(getFinder(parameters), parameters);
        return new FitterFunctionDifferences(fitter);
    }

    public static FitterFunctionOutput produceOutput(FittingParameters parameters) {
        MemoryFitterOutput fitter = MemoryFitterOutput.of(getFinder(parameters), parameters);
        return new FitterFunctionOutput(fitter);
    }

    private static BucketFinder getFinder(FittingParameters parameters) {
        double range= parameters.range();
        double dx= parameters.deltaX();
        return BucketFinder.of(Range.closed(-range, range), dx);  //closed <=> inclusive
    }

}
