package chapters.ch9.plotting;

import lombok.Builder;

@Builder
public record MeasuresOneDimRegressionNeural(
        double error,
        double valueLeft,
        double valueRight)
 {
 }
