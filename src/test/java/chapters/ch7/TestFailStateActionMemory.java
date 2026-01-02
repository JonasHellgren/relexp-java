package chapters.ch7;

import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import chapters.ch7.domain.safety_layer.FailStateActionMemory;
import chapters.ch7.factory.TrainerDependencySafeFactory;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TestFailStateActionMemory {

     TrainerGridDependencies dependencies;
     StateGrid state;
     ActionGrid action, actionCorrected;
     StateActionGrid stateAction;

    @BeforeEach
     void setup() {
        dependencies = TrainerDependencySafeFactory.treasureForTest();
        state = StateGrid.of(0, 0);
        action = ActionGrid.E;
        actionCorrected = ActionGrid.W;
        stateAction = StateActionGrid.of(state, action);
    }

    @Test
     void testEmptyMemory() {
        var memory = FailStateActionMemory.empty(dependencies);
        assertFalse(memory.contains(stateAction));
        assertTrue(memory.getNonFailActions(state).contains(action));
        assertEquals(0, memory.size());
    }

    @Test
     void testAddAndContains() {
        var memory = FailStateActionMemory.empty(dependencies);
        memory.add(stateAction);
        assertTrue(memory.contains(stateAction));
        assertFalse(memory.getNonFailActions(state).contains(action));
        assertEquals(1, memory.size());
    }

    @Test
     void testGetNonFailActions() {
        var memory = FailStateActionMemory.empty(dependencies);
        memory.add(stateAction);
        var nonFailActions = memory.getNonFailActions(state);
        assertFalse(nonFailActions.contains(action));
        assertFalse(nonFailActions.isEmpty());
        assertEquals(1, memory.size());
    }

    @Test
     void testOf() {
        Set<StateActionGrid> failMemory = new HashSet<>();
        failMemory.add(stateAction);
        var memory = FailStateActionMemory.of(dependencies, failMemory);
        assertTrue(memory.contains(stateAction));
    }

    @Test
    void testAddSameTwice() {
        var memory = FailStateActionMemory.empty(dependencies);
        memory.add(stateAction);
        memory.add(stateAction);
        assertEquals(1, memory.size());
    }

    @Test
    void testAddTwoDifferent() {
        var memory = FailStateActionMemory.empty(dependencies);
        memory.add(stateAction);
        memory.add(StateActionGrid.of(state,ActionGrid.S));
        assertEquals(2, memory.size());
    }

}
