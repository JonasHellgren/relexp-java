package chapters.ch2.implem.function_fitting;

import core.foundation.util.math.MyMathUtils;
import lombok.experimental.UtilityClass;
import java.util.List;

@UtilityClass
public class FitterOutCalculator {

    public static List<Double> produceOutput(FitterFunctionI fitter,
                                             List<Double> xList,
                                             FittingParameters parameters) {
        double range= parameters.range();
        double margin= parameters.margin();
        return xList.stream()
                .map(x -> fitter.read(MyMathUtils.clip(x,-range,range- margin)))
                .toList();
    }
}
