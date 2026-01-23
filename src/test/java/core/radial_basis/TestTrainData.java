package core.radial_basis;

import core.nextlevelrl.radial_basis.TrainData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class TestTrainData {


    List<double[]> input;

    @BeforeEach
      void init() {
        double[] inArr = {1.0, 2.0, 3.0};
        input = List.of(inArr);
      }

    @Test
    void testEmpty() {
        TrainData data = TrainData.empty();
        Assertions.assertTrue(data.inputs().isEmpty());
        Assertions.assertTrue(data.outputs().isEmpty());
    }

    @Test
    void testAddIAndOut() {
        double output = 42.0;
        TrainData data = TrainData.empty();
        double[] inArr = {1.0, 2.0, 3.0};
        data.addIAndOut(inArr, output);
        Assertions.assertEquals(inArr, data.inputs().get(0));
        Assertions.assertEquals(output, data.outputs().get(0));
    }

    @Test
    void testClear() {
        TrainData data = TrainData.empty();
        data.clear();
        Assertions.assertTrue(data.inputs().isEmpty());
        Assertions.assertTrue(data.outputs().isEmpty());
    }

    @Test
    void testNsamples() {
        double output = 42.0;
        TrainData data = TrainData.of(input, List.of(output));
        Assertions.assertEquals(1, data.nSamples());
    }

    @Test
    void testInput() {
        double output = 42.0;
        TrainData data = TrainData.of(input, List.of(output));
        Assertions.assertEquals(input.get(0), data.input(0));
    }

    @Test
    void testOutput() {
        double output = 42.0;
        TrainData data = TrainData.of(input, List.of(output));
        Assertions.assertEquals(output, data.output(0));
    }

    @Test
    void testCheckIndex() {
        double output = 42.0;
        TrainData data = TrainData.of(input, List.of(output));
        Assertions.assertThrows(IllegalArgumentException.class, () -> data.input(1));
    }

}
