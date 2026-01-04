package chapters.ch12;

import chapters.ch12.inv_pendulum.domain.environment.core.StatePendulum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestStatePendulum {

    @Test
    void testOfStart() {
        var state = StatePendulum.ofStart(1.0, 2.0);
        assertEquals(1.0, state.angle(), 0.01);
        assertEquals(2.0, state.angularSpeed(), 0.01);
        assertEquals(0, state.nSteps());
    }

    @Test
    void testOf() {
        var state = StatePendulum.of(1.0, 2.0, 3);
        assertEquals(1.0, state.angle(), 0.01);
        assertEquals(2.0, state.angularSpeed(), 0.01);
        assertEquals(3, state.nSteps());
    }

    @Test
    void testToString() {
        var state = StatePendulum.of(1.1, 2.1111, 3);
        String expected = "State{nOccupied=1.1, angularSpeed=2.111}";
        assertEquals(expected, state.toString());
    }
}
