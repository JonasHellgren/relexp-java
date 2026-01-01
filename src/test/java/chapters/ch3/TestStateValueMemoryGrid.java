package chapters.ch3;

import chapters.ch3.implem.splitting_path_problem.StateValueMemoryGrid;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestStateValueMemoryGrid {

    static final double DEFAULT_STATE_VALUE = 0.0;
    StateGrid state01;
    StateValueMemoryGrid memory;

    @BeforeEach
    void init() {
        state01 = StateGrid.of(0, 1);
        memory = StateValueMemoryGrid.of(DEFAULT_STATE_VALUE);
    }

    @Test
    void testConstructor() {
        assertTrue(memory.isEmpty());
    }

    @Test
    void testRead() {
        StateGrid state = state01; // assuming StateGrid has a no-arg constructor
        assertEquals(DEFAULT_STATE_VALUE, memory.read(state));
    }

    @Test
    void testWrite() {
        StateGrid state = state01; // assuming StateGrid has a no-arg constructor
        double value = 1.0;
        memory.write(state, value);
        assertEquals(value, memory.read(state));
    }

    @Test
    void testFit() {
        StateGrid state = state01; // assuming StateGrid has a no-arg constructor
        double valueTar = 1.0;
        double learningRate = 0.1;
        double value0 = memory.read(state);
        double err = memory.fit(state, valueTar, learningRate);
        double value = memory.read(state);
        assertTrue(value > value0);
        assertTrue(err > 0.0);
    }


}
