package chapters.ch7;

import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch4.domain.trainer.core.ExperienceGrid;
import chapters.ch7.domain.fail_learner.FailLearnerActive;
import chapters.ch7.domain.fail_learner.FailLearnerI;
import chapters.ch7.domain.fail_learner.FailLearnerPassive;
import chapters.ch7.domain.safety_layer.SafetyLayer;
import chapters.ch7.domain.trainer.ExperienceGridCorrectedAction;
import chapters.ch7.factory.TrainerDependencySafeFactory;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestFailLearner {

    FailLearnerI learnerPassive, learnerActive;
    SafetyLayer safetyLayer;
    List<ExperienceGridCorrectedAction> experiences;

    @BeforeEach
    void init() {
        var dependencies = TrainerDependencySafeFactory.treasureForTest();
        safetyLayer = SafetyLayer.empty(dependencies);
        learnerPassive = FailLearnerPassive.create();
        learnerActive = FailLearnerActive.create();
        experiences = getMockedExperiences();
    }

    @Test
    void givenNonTrainedLayer_thenEmptyMemory() {
        assertEquals(0, safetyLayer.memorySize());
    }

    @Test
    void givenPassiveLearner_thenEmptyMemory() {
        learnerPassive.updateLayer(safetyLayer, experiences);
        assertEquals(0, safetyLayer.memorySize());
    }

    @Test
    void givenActiveLearner_thenNonEmptyMemory() {
        learnerActive.updateLayer(safetyLayer, experiences);
        var failMemory = safetyLayer.getFailMemory();
        assertEquals(1, safetyLayer.memorySize());
        assertTrue(failMemory.contains(StateActionGrid.of(1, 1, ActionGrid.N)));
    }

    private List<ExperienceGridCorrectedAction> getMockedExperiences() {
        var srNotFail = StepReturnGrid.ofNotTerminal(StateGrid.of(1, 0), 0.0);
        var srFail = StepReturnGrid.ofTerminalFail(StateGrid.of(1, 0), 0.0);
        var ex0y1 = ExperienceGrid.ofSars(StateGrid.of(0, 1), ActionGrid.E, srNotFail);
        var ex1y1 = ExperienceGrid.ofSars(StateGrid.of(1, 1), ActionGrid.E, srNotFail);
        var e11fail = ExperienceGrid.ofSars(StateGrid.of(1, 1), ActionGrid.N, srFail);  //fail experience!!!
        return List.of(
                ExperienceGridCorrectedAction.of(ex0y1,ActionGrid.E),
                ExperienceGridCorrectedAction.of(ex1y1,ActionGrid.E),
                ExperienceGridCorrectedAction.of(e11fail,ActionGrid.N));
    }


}
