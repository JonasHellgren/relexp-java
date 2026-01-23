package chapters.ch10.plotting;

import chapters.ch10.cannon.domain.trainer.ExperienceCannon;
import core.foundation.gadget.math.MeanAndStd;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
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


    public static MeasuresCannon getMeasures(double gMinusBase,
                                             double base,
                                             ExperienceCannon experience,
                                             GradientMeanAndLogStd gradLog,
                                             MeanAndStd meanAndStd) {
        return MeasuresCannon.builder()
                .returnMinusBase(gMinusBase).base(base)
                .angle(experience.action()).distance(experience.stepReturn().distance())
                .gradLogZMean(gradLog.mean()).gradLogZStd(gradLog.std())
                .mean(meanAndStd.mean()).std(meanAndStd.std())
                .build();
    }

}
