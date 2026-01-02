package core.foundation.util.math;

import java.util.function.Function;

public final class MyFunctions {

    private MyFunctions() {
    }

    public static final Function<Double,Double> sqr2 =(n) -> Math.pow(n,2);
    public static final Function<Double,Double> sqr3=(n) -> Math.pow(n,3);


}
