package core.foundation.gadget.math;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.math3.util.Pair;

/***
 *
 * https://en.wikipedia.org/wiki/Exponential_decay
 *
 * out(t)=e^(C)*e^(-gamma*t)
 *
 * the parameters C and gamma are set from start and end values of out, i.e. outStart=out(0) and outEnd=out(timeEnd)
 */

@ToString
@Getter
public class LogarithmicDecay {

    public static final double SMALL = 1e-20;
    private final double timeEnd;
    private double C, gamma;

    public LogarithmicDecay(double outStart, double outEnd, double timeEnd) {
        this.timeEnd=timeEnd;
        defineParameters(outStart, outEnd, timeEnd);
    }

    public static LogarithmicDecay of(double outStart, double outEnd, double timeEnd) {
        return new LogarithmicDecay(outStart, outEnd, timeEnd);
    }

    public static LogarithmicDecay of(@NonNull  Pair<Double, Double> outStartAndEnd, double timeEnd) {
        return new LogarithmicDecay(outStartAndEnd.getFirst(), outStartAndEnd.getSecond(), timeEnd);
    }

    private void defineParameters(double outStart, double outEnd, double timeEnd) {
        Preconditions.checkArgument(timeEnd>0, "End time must be positive");

        this.C=Math.log(outStart);
        double minusGammaTime=Math.log(Math.max(SMALL, outEnd)/Math.exp(C));
        this.gamma=-minusGammaTime/timeEnd;
    }

    /**
     * Calculates the output value at the specified time.
     *
     * This method delegates to the overloaded calcOut method that takes a double time parameter.
     *
     * @param time the time at which to calculate the output value
     * @return the calculated output value
     */
    public double calcOut(int time) {
        return calcOut((double)time);
    }

    /**
     * Calculates the output value at the specified time using the exponential decay formula.
     *
     * @param time the time at which to calculate the output value
     * @return the calculated output value
     */
    public double calcOut(double time) {
        Preconditions.checkArgument(time>=0, "Time must be positive");
        Preconditions.checkArgument(time<=timeEnd, "Time must be less than timeEnd");
        return Math.exp(C)*Math.exp(-gamma*time);
    }

}
