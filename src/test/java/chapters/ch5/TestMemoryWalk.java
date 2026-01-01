package chapters.ch5;

import chapters.ch5.implem.walk.MemoryWalk;
import chapters.ch5.implem.walk.StateWalk;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestMemoryWalk {


    @Test
     void testCreate() {
        MemoryWalk memoryWalk = MemoryWalk.create();
        assertNotNull(memoryWalk);
        assertTrue(memoryWalk.isEmpty());
    }

    @Test
     void testReadDefaultValue() {
        var memoryWalk = MemoryWalk.create();
        var state = StateWalk.of(0); // Assuming StateWalk has a no-arg constructor
        assertEquals(MemoryWalk.DEFAULT_VALUE, memoryWalk.read(state));
    }

    @Test
     void testReadWrittenValue() {
        var memoryWalk = MemoryWalk.create();
        var state =StateWalk.of(0); // Assuming StateWalk has a no-arg constructor
        double value = 10.0;
        memoryWalk.write(state, value);
        assertEquals(value, memoryWalk.read(state));
    }

    @Test
     void givenTwoStates_testReadWrittenValue() {
        var memoryWalk = MemoryWalk.create();
        var state0 =StateWalk.of(0); // Assuming StateWalk has a no-arg constructor
        var state1 =StateWalk.of(1); // Assuming StateWalk has a no-arg constructor
        double value0 = 10.0;
        double value1 = 5.0;
        memoryWalk.write(state0, value0);
        memoryWalk.write(state1, value1);
        assertEquals(value0, memoryWalk.read(state0));
        assertEquals(value1, memoryWalk.read(state1));
    }


}
