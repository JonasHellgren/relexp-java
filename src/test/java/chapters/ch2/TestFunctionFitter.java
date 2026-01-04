package chapters.ch2;

import chapters.ch2.factory.FitterFunctionFactory;
import chapters.ch2.factory.FittingParametersFactory;
import chapters.ch2.impl.function_fitting.FitterFunctionOutput;
import core.foundation.gadget.training.TrainData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFunctionFitter {

    public static final double TOL = 1e-3;
    public static final int N_EPOCHS = 1000;
    FitterFunctionOutput fitter;

    @BeforeEach
    void init() {
        fitter = FitterFunctionFactory.produceOutput(FittingParametersFactory.produceDefault());
    }

    @Test
    void whenFitted_thenCorrect() {
        var data = TrainData.emptyFromOutputs();
        data.addIAndOut(List.of(-10d), 0.0);
        data.addIAndOut(List.of(0d), 0.5);
        data.addIAndOut(List.of(10d), 1.0);
        IntStream.range(0, N_EPOCHS).forEach(i -> fitter.fit(data));
        assertEquals(3,fitter.getMemory().size());
        assertEquals(0.0, fitter.read(-10), TOL);
        assertEquals(0.5, fitter.read(0), TOL);
        assertEquals(1.0, fitter.read(10), TOL);
    }

}
