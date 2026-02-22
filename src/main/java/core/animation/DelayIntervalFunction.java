package core.animation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DelayIntervalFunction {

    /** [cuts[i], cuts[i+1]) -> values[i], last: [cuts[last], +inf) */
    public static DoubleUnaryOperator from(IntervalData data) {
        double[] cuts = data.cuts();
        double[] values = data.values();

        return x -> {
            if (x < cuts[0]) throw new IllegalArgumentException("x below domain: " + x);
            int pos = Arrays.binarySearch(cuts, x);
            int idx = (pos >= 0) ? pos : (-pos - 2);
            return values[idx];
        };
    }
}
