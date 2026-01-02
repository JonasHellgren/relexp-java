package chapters.ch9;

import chapters.ch9.gradient_descent.LinearFitter;
import chapters.ch9.gradient_descent.PhiExtractor;
import core.foundation.gadget.training.TrainData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class TestLinearFitter {

    public static final double LEARNING_RATE = 0.9;
    public static final int N_EPOCHS = 100;
    public static final double TOL = 0.1;
    LinearFitter fitter;

    @BeforeEach
    void init() {
        var phiExtractor = PhiExtractor.empty();
        phiExtractor.functionList.add(x -> 1);
        phiExtractor.functionList.add(x -> x.get(0));
        var data = TrainData.emptyFromOutputs();
        data.addIAndOut(List.of(0.1), 5.0 + 1.0);
        data.addIAndOut(List.of(0.3), 5.0 + 3.0);
        data.addIAndOut(List.of(0.5), 5.0 + 5.0);
        data.addIAndOut(List.of(0.8), 5.0 + 8.0);
        data.addIAndOut(List.of(1.0), 5.0 + 10.0);
        fitter = LinearFitter.of(data, phiExtractor, LEARNING_RATE);
    }

    @Test
    void givenData_whenFitFromSinglelSample_thenCorrectWeights() {
        fitter.fit(N_EPOCHS, 1);
        var weights = fitter.getWeights();
        Assertions.assertEquals(5.0, weights.get(0), TOL);
        Assertions.assertEquals(10.0, weights.get(1), TOL);
    }

    @Test
    void givenData_whenFitFromALlSamples_thenCorrectWeights() {
        fitter.fit(N_EPOCHS, fitter.getData().nSamples());
        var weights = fitter.getWeights();
        Assertions.assertEquals(5.0, weights.get(0), TOL);
        Assertions.assertEquals(10.0, weights.get(1), TOL);
    }

    @Test
    void givenData_whenFitFromALlSamples_thenCorrectOutput() {
        fitter.fit(N_EPOCHS, fitter.getData().nSamples());
        Assertions.assertEquals(5.0 + 1.0, fitter.calcOut(List.of(0.1)), TOL);
        Assertions.assertEquals(5.0 + 8.0, fitter.calcOut(List.of(0.8)), TOL);
    }

    @Test
    void givenData_whenFitFromALlSamples_thenCorrectOutputs() {
        fitter.fit(N_EPOCHS, fitter.getData().nSamples());
        var outputs = fitter.calcOutputs(List.of(List.of(0.1), List.of(0.8)));
        Assertions.assertEquals(5.0 + 1.0, outputs.get(0), TOL);
        Assertions.assertEquals(5.0 + 8.0, outputs.get(1), TOL);
    }

}
