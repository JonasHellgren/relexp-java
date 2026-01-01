package core.foundation.util.math;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Accumulator {

    double value;

    public static Accumulator empty() {
        return new Accumulator(0);
    }

    public void add(double v) {
        value += v;
    }

    public double value() {
        return value;
    }

    public void reset() {
        value = 0;
    }

}
