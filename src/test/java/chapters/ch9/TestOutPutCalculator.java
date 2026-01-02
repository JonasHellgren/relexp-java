package chapters.ch9;

import chapters.ch9.gradient_descent.OutPutCalculator;
import chapters.ch9.gradient_descent.PhiExtractor;
import core.foundation.gadget.training.Weights;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

 class TestOutPutCalculator {


    public static final double TOL = 0.001;

    @Test
     void testOutPutCalculatorCreation() {
        PhiExtractor phiExtractor = PhiExtractor.empty();
        OutPutCalculator outPutCalculator = OutPutCalculator.of(phiExtractor);
        assertNotNull(outPutCalculator);
    }

    @Test
     void testOutPutCalculatorOutPut() {
        PhiExtractor phiExtractor = PhiExtractor.of(Arrays.asList(
                (input) -> 1.0,
                (input) -> 2.0
        ));
        OutPutCalculator outPutCalculator = OutPutCalculator.of(phiExtractor);
        Weights weights = Weights.of(List.of(1.0, 2.0));
        List<Double> input = Arrays.asList(1.0, 2.0);
        double expectedOutput = 1.0 * 1.0 + 2.0 * 2.0;
        assertEquals(expectedOutput, outPutCalculator.outPut(weights, input), TOL);
    }

    @Test
     void testOutPutCalculatorOutPutWithZeroWeights() {
        PhiExtractor phiExtractor = PhiExtractor.of(Arrays.asList(
                (input) -> 1.0,
                (input) -> 2.0
        ));
        OutPutCalculator outPutCalculator = OutPutCalculator.of(phiExtractor);
        Weights weights = Weights.of(List.of(0.0, 0.0));
        List<Double> input = Arrays.asList(1.0, 2.0);
        double expectedOutput = 0.0;
        assertEquals(expectedOutput, outPutCalculator.outPut(weights, input), TOL);
    }

    @Test
     void testOutPutCalculatorOutPutWithZeroInput() {
        PhiExtractor phiExtractor = PhiExtractor.of(Arrays.asList(
                (input) -> 1.0,
                (input) -> input.get(0)
        ));
        OutPutCalculator outPutCalculator = OutPutCalculator.of(phiExtractor);
        Weights weights=Weights.of(List.of(1.0, 2.0));
        List<Double> input = Arrays.asList(1.0);
        double expectedOutput = 1*1d+2*1d;
        assertEquals(expectedOutput, outPutCalculator.outPut(weights, input), TOL);
    }

}
