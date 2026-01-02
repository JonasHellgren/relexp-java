package chapters.ch10.cannon.domain.agent;

import core.foundation.gadget.math.MeanAndStd;
import core.foundation.gadget.math.MeanAndStdMemoryParameters;
import lombok.Builder;
import lombok.With;

/**
 * Represents the parameters of an agent in the cannon domain.
 */

 @Builder
@With
public record AgentParametersCannon(
        MeanAndStd meanAndStdInit,
        MeanAndStd meanAndStdMin,
        MeanAndStd meanAndStdMax,
        Double gradzMeanMax,
        double gradzStdMax
) {


    public double minZStd() {
        return log(meanAndStdMin.std());
    }

    public double maxZStd() {
        return log(meanAndStdMax.std());
    }

    public MeanAndStdMemoryParameters zInit() {
        return MeanAndStdMemoryParameters.of(meanAndStdInit.mean(), log(meanAndStdInit.std()));
    }


    private double log(double std) {
        return Math.log(std);
    }

}
