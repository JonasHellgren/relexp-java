package chapters.ch9;

import chapters.ch9.gradient_descent.PhiExtractor;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

 class TestPhiExtractor {

     public static final double TOL = 0.001;

     @Test
     void testConstructor() {
        List<ToDoubleFunction<List<Double>>> functionList = new ArrayList<>();
        PhiExtractor phiExtractor = PhiExtractor.of(functionList);
        assertNotNull(phiExtractor);
    }

    @Test
     void testNPhis() {
        List<ToDoubleFunction<List<Double>>> functionList = new ArrayList<>();
        functionList.add(x -> 1.0);
        functionList.add(x -> 2.0);
        PhiExtractor phiExtractor = PhiExtractor.of(functionList);
        assertEquals(2, phiExtractor.nPhis());
    }

    @Test
     void testGetPhi() {
        List<ToDoubleFunction<List<Double>>> functionList = new ArrayList<>();
        ToDoubleFunction<List<Double>> fcn0 = x -> 1;
        functionList.add(fcn0);
        ToDoubleFunction<List<Double>> fcn1 = x -> x.get(0);
        functionList.add(fcn1);
        PhiExtractor phiExtractor = PhiExtractor.of(functionList);
        double x = 0.0;
        var input=List.of(x);
        assertEquals(1.0, phiExtractor.getPhi(input, 0), TOL);
        assertEquals(x, phiExtractor.getPhi(input, 1), TOL);
    }

     @Test
     void testGetPhi_emptyConstructor() {
         ToDoubleFunction<List<Double>> fcn0 = x -> 1;
         ToDoubleFunction<List<Double>> fcn1 = x -> x.get(0);
         PhiExtractor phiExtractor = PhiExtractor.empty();
         phiExtractor.functionList.add(fcn0);
         phiExtractor.functionList.add(fcn1);
         double x = 0.0;
         var input=List.of(x);
         assertEquals(1.0, phiExtractor.getPhi(input, 0), TOL);
         assertEquals(x, phiExtractor.getPhi(input, 1), TOL);
     }


}
