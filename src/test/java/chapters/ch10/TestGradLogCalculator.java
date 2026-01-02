package chapters.ch10;

import chapters.ch10.bandit._shared.GradLogCalculatorDiscreteActions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class TestGradLogCalculator {

    public static final double TOL = 1e-6;

    @Test
     void test0d50d5() {
        int indexAction = 0;
        double[] probArray = {0.5, 0.5};
        double[] expected = {0.5, -0.5};
        double[] result = GradLogCalculatorDiscreteActions.calc(indexAction, probArray);
        assertArrayEquals(expected, result, TOL);
    }

    @Test
    void test0d50d5_action1() {
        int indexAction = 1;
        double[] probArray = {0.5, 0.5};
        double[] expected = {-0.5, 0.5};
        double[] result = GradLogCalculatorDiscreteActions.calc(indexAction, probArray);
        assertArrayEquals(expected, result, TOL);
    }


    @Test
    void testAsWorkedExample10d3() {
        int indexAction = 0;
        double[] probArray = {0.7311, 0.2689};
        double[] expected = {0.2689, -0.2689};
        double[] result = GradLogCalculatorDiscreteActions.calc(indexAction, probArray);
        assertArrayEquals(expected, result, TOL);
    }

}
