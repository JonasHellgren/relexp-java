package chapters.ch2.impl.function_fitting;

import chapters.ch2.domain.FitterFunctionI;
import chapters.ch2.domain.FittingParameters;
import chapters.ch2.domain.MemoryFitterI;
import core.foundation.util.math.MyMathUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FitterOutCalculator {

    MemoryFitterI fitter;

    public static FitterOutCalculator of(MemoryFitterI fitter) {
        return new FitterOutCalculator(fitter);
    }

    public  List<Double> produceOutput(List<Double> xList,
                                             FittingParameters parameters) {
        double range= parameters.range();
        double margin= parameters.margin();
        return xList.stream()
                .map(x -> fitter.read(MyMathUtils.clip(x,-range,range- margin)))
                .toList();
    }
}
