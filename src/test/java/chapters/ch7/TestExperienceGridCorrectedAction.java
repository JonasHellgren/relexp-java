package chapters.ch7;

import chapters.ch7.domain.trainer.ExperienceGridCorrectedAction;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestExperienceGridCorrectedAction {

    StateGrid state;
    ActionGrid action;
    ActionGrid actionCorrected;
    StepReturnGrid sr;

    @BeforeEach
    void init() {
        state = StateGrid.of(0, 0);
        action = ActionGrid.E;
        actionCorrected = ActionGrid.W;
        sr = StepReturnGrid.ofNotTerminal(StateGrid.of(1, 0), 0.0);
    }

    @Test
    void testOfSars() {
        var experience = ExperienceGridCorrectedAction.ofSars(state, action, actionCorrected, sr);
        assertEquals(action, experience.actionAgent());
        assertEquals(actionCorrected, experience.actionCorrected());
        assertEquals(action, experience.actionAgent());
    }

    @Test
    void testIsCorrected() {
        var expCorrected = ExperienceGridCorrectedAction.ofSars(state, action, actionCorrected, sr);
        var expNotCorrected = ExperienceGridCorrectedAction.ofSars(state, action, action, sr);
        assertTrue(expCorrected.isCorrected());
        assertFalse(expNotCorrected.isCorrected());
    }

}
