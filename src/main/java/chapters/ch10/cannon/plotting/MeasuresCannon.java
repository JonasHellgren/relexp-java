package chapters.ch10.cannon.plotting;

import lombok.Builder;

/**
 * Represents a set of measures for the cannon environment. Candidates for plotting.
 *
 */

@Builder
public record MeasuresCannon(
        double returnMinusBase,
        double base,
        double angle,
        double distance,
        double gradLogZMean,
        double gradLogZStd,
        double mean,
        double std
) {
}
