package chapters.ch9.neural.plotting;

import lombok.Builder;

@Builder
public record MeasuresOneDimRegressionNeural(
        double error,
        double valueLeft,
        double valueRight)
 {
 }
