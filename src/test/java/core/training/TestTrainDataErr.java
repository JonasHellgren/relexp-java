package core.training;

import core.foundation.gadget.training.TrainDataErr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TestTrainDataErr {


    @Test
    void testEmpty() {
        TrainDataErr testDataErr = TrainDataErr.empty();
        Assertions.assertTrue(testDataErr.isEmpty());
        Assertions.assertEquals(0, testDataErr.nSamples());
    }

    @Test
    void testAdd() {
        List<double[]> inputs = new ArrayList<>();
        inputs.add(new double[]{1.0, 2.0, 3.0});
        List<Double> errors = new ArrayList<>();
        errors.add(4.0);

        TrainDataErr testDataErr = TrainDataErr.of(inputs, errors);
        testDataErr.add(new double[]{4.0, 5.0, 6.0}, 7.0);

        Assertions.assertEquals(2, testDataErr.nSamples());
        Assertions.assertEquals(3, testDataErr.input(1).length);
        Assertions.assertEquals(7.0, testDataErr.error(1));
    }

    @Test
    void testCreateBatch() {
        List<double[]> inputs = new ArrayList<>();
        inputs.add(new double[]{1.0, 2.0, 3.0});
        inputs.add(new double[]{1.0, 22.0, 2.0});
        List<Double> errors = new ArrayList<>();
        errors.add(4.0);
        errors.add(5.0);

        TrainDataErr testDataErr = TrainDataErr.of(inputs, errors);
        TrainDataErr batch = testDataErr.createBatch(1);

        Assertions.assertEquals(1, batch.nSamples());
    }


}
