package chapters.ch8;

import chapters.ch8.domain.environment.core.StateParking;
import org.junit.jupiter.api.Test;

import static chapters.ch8.domain.environment.core.FeeEnum.NotCharging;
import static org.junit.jupiter.api.Assertions.assertEquals;

 class TestStateParking {

    @Test
     void testOfStart() {
        StateParking state = StateParking.ofStart(10, NotCharging);
        assertEquals(10, state.nOccupied());
        assertEquals(NotCharging, state.fee());
        assertEquals(0, state.nSteps());
    }

    @Test
     void testOf() {
        StateParking state = StateParking.of(10, NotCharging, 3);
        assertEquals(10, state.nOccupied());
        assertEquals(NotCharging, state.fee());
        assertEquals(3, state.nSteps());
    }

    @Test
     void testToString() {
        StateParking state = StateParking.of(10, NotCharging, 3);
        String expected = "VariablesParking[" +
                "nOccupied=" + 10 +
                ", fee=" + NotCharging +
                ']';
        assertEquals(expected, state.toString());
    }

    @Test
     void testZeroSteps() {
        assertEquals(0, StateParking.ZERO_STEPS);
    }
}