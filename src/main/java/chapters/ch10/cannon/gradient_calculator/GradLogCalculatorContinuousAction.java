package chapters.ch10.cannon.gradient_calculator;


import chapters.ch10.cannon.domain.trainer.TrainerParametersCannon;
import core.foundation.gadget.math.MeanAndStd;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
import core.nextlevelrl.gradient.NormalDistributionGradientCalculator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

/***
 * Wraps NormalDistributionGradientCalculator
 * Calculating gradients of the log probability of a continuous action.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GradLogCalculatorContinuousAction {

    NormalDistributionGradientCalculator calculator;

    public static GradLogCalculatorContinuousAction of(TrainerParametersCannon tp) {
        return new GradLogCalculatorContinuousAction(
                NormalDistributionGradientCalculator.of(tp.denomMinGradLog()));
    }
    /**
     * Calculates the gradient of the log probability of the given action with respect to the mean and standard
     * deviation of the normal distribution.
     *
     * @param action the action value
     * @param meanAndStd the mean and standard deviation of the normal distribution
     * @return the gradient of the mean and log standard deviation
     */
    public GradientMeanAndLogStd gradLog(double action, MeanAndStd meanAndStd) {
        var msAsPair=new Pair<>(meanAndStd.mean(),meanAndStd.std());
        var pair= calculator.gradient(action, msAsPair);
        return GradientMeanAndLogStd.of(pair.getFirst(),pair.getSecond());
    }

}
