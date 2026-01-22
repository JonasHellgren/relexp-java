package core.foundation.util.math;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public final class FunctionsUtil {

    public static final Function<Double,Double> sqr2 =(n) -> Math.pow(n,2);
    public static final Function<Double,Double> sqr3=(n) -> Math.pow(n,3);


}
