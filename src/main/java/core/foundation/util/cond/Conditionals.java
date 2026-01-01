package core.foundation.util.cond;

import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;

public class Conditionals {

    /**
     * ifTrueMethod is executed if condition is true, else is ifFalseMethod executed
     * Handy to avoid ugly branching: if (condition) { trueMethod() } else { falseMethod() }
     * can be replaced by cleaner executeDependantOnCondition(condition,trueMethod,falseMethod)
     */
    public static void executeOneOfTwo(boolean condition, Runnable ifTrueMethod, Runnable ifFalseMethod) {
        (condition ? ifTrueMethod : ifFalseMethod).run();
    }

    /**
     * Following method is used if we want to run ifTrueMethod only if condition is true
     */
    public static void executeIfTrue(boolean condition, Runnable ifTrueMethod) {
        executeOneOfTwo(condition, ifTrueMethod, () -> {});
    }

    public static void executeIfFalse(boolean condition, Runnable ifTrueMethod) {
        executeOneOfTwo(!condition, ifTrueMethod, () -> {});
    }


    public static final BiFunction<Boolean,Double,Double> zeroIfTrueElseNum=(cond, num) -> (cond) ? 0 : num;
    public static final BiFunction<Boolean,Double,Double> numIfTrueElseZero=(cond,num) -> (cond) ? num : 0d;

    public static BiFunction<Double,Double,Double> secondArgIfSmaller =(num, elseNum) ->
            (Math.abs(num)< elseNum) ? elseNum :num;

    public static final TriFunction<Boolean,Object,Object,Object> secIfFalse =(cond, ifTrue, ifFalse) ->
            Boolean.TRUE.equals(cond) ? ifTrue:ifFalse;

    public static final TriFunction<Boolean,Double,Double,Double> secDoubleIfFalse =(cond, ifTrue, ifFalse) ->
            Boolean.TRUE.equals(cond) ? ifTrue:ifFalse;



}
