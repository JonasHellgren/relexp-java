package chapters.ch2.factory;

import chapters.ch2.domain.fitting.FittingParameters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FittingParametersFactory {

    public static FittingParameters produceDefault() {
        return FittingParameters.builder()
                .learningRate(0.1)
                .nofIterations(100)
                .defaultMemoryValue(0.0)
                .range(10.0)
                .deltaX(1.0)
                .margin(0.2)
                .build();
    }
}
