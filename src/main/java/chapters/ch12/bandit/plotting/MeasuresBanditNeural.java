package chapters.ch12.bandit.plotting;

import lombok.Builder;

@Builder
public record MeasuresBanditNeural(
        double error,
        double valueLeft,
        double valueRight)
 {
 }
