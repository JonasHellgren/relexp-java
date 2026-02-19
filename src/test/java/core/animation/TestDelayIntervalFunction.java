package core.animation;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import static org.junit.jupiter.api.Assertions.*;

public class TestDelayIntervalFunction {


    @Test
    void shouldReturnCorrectValuesInsideIntervals() {

        var data = IntervalData.of(
                List.of(0.0, 50.0, 950.0),  //cuts
                List.of(80.0, 10.0, 80.0)   //values
        );

        DoubleUnaryOperator f = DelayIntervalFunction.from(data);

        assertEquals(80.0, f.applyAsDouble(0));
        assertEquals(80.0, f.applyAsDouble(49.999));
        assertEquals(10.0, f.applyAsDouble(50));
        assertEquals(10.0, f.applyAsDouble(700));
        assertEquals(80.0, f.applyAsDouble(950));
        assertEquals(80.0, f.applyAsDouble(5000));
    }

    @Test
    void shouldThrowBelowDomain() {

        var data = new IntervalData(
                List.of(10.0, 20.0),
                List.of(5.0, 1.0)
        );

        DoubleUnaryOperator f = DelayIntervalFunction.from(data);

        assertThrows(IllegalArgumentException.class,
                () -> f.applyAsDouble(9.999));
    }

    @Test
    void boundaryBehaviorClosedOpen() {

        var data = new IntervalData(
                List.of(0.0, 5.0),
                List.of(1000.0, 0.0)
        );

        DoubleUnaryOperator f = DelayIntervalFunction.from(data);

        // [0,5)
        assertEquals(1000.0, f.applyAsDouble(0.0));
        assertEquals(1000.0, f.applyAsDouble(4.9999));

        // [5,∞)
        assertEquals(0.0, f.applyAsDouble(5.0));
    }
}


