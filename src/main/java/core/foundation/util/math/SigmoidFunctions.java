package core.foundation.util.math;

import java.util.function.DoubleUnaryOperator;

public class SigmoidFunctions {

    public static DoubleUnaryOperator sigmoid=(x) ->  1.0 / (1.0 + Math.exp(-x));

    public static DoubleUnaryOperator derSigmoid = (x) ->
    {
        double sig = sigmoid.applyAsDouble(x);
        return sig * (1 - sig);
    };


}
