package chapters.ch2;

import chapters.ch2.factory.FittingParametersFactory;
import chapters.ch2.impl.parameter_fitting.FitterSingleParameter;
import core.foundation.gadget.training.TrainData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFitterSingleParameter {

    public static final double TOL = 1e-3;
    public static final int N_EPOCHS = 1000;
    public static final double DUMMY_IN = 0.0;
    FitterSingleParameter fitter;

    @BeforeEach
    void init() {
        fitter = FitterSingleParameter.of(FittingParametersFactory.produceDefault());
    }

    @Test
    void whenFitted_thenCorrect() {
        var data = TrainData.empty();
        data.addListIn(List.of(DUMMY_IN), 1.0);
        IntStream.range(0, N_EPOCHS).forEach(i -> fitter.fit(data));
        assertEquals(1.0, fitter.read(DUMMY_IN), TOL);
    }

}
