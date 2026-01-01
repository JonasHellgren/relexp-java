package core.foundation.util.math;

/**
 * https://en.wikipedia.org/wiki/Logistic_function
 */

public class LogisticFunction {

    private LogisticFunction() {
    }

    public static double logistic(double x, double x0, double k) {
        return 1/(1+Math.exp(-k*(x-x0)));
    }


    public static double invertedLogistic(double x, double x0, double k) {
        return 1-logistic(x,x0,k);
    }

}
