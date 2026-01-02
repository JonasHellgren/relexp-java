package chapters.ch7;

import chapters.ch4.domain.agent.AgentQLearningGrid;
import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import chapters.ch7.domain.trainer.AgentMemoryPenalizerCorrectedAction;
import chapters.ch7.domain.trainer.ExperienceGridCorrectedAction;
import chapters.ch7.factory.TrainerDependencySafeFactory;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestAgentMemoryPenalizerCorrectedAction {

    List<ExperienceGridCorrectedAction> experiences;
    AgentMemoryPenalizerCorrectedAction penalizer;
    TrainerGridDependencies dependencies;

    @BeforeEach
    void init() {
        var state = StateGrid.of(1, 0);
        var action = ActionGrid.N;
        var actionCorrected = ActionGrid.E;
        var sr = StepReturnGrid.ofNotTerminal(StateGrid.of(2, 0), 0d);
        var experience = ExperienceGridCorrectedAction.ofSars(state, action, actionCorrected, sr);
        experiences = new ArrayList<>();
        experiences.add(experience);
        dependencies = TrainerDependencySafeFactory.treasureForTest();
        penalizer = AgentMemoryPenalizerCorrectedAction.of(dependencies);
    }

    @Test
    void testPenalize() {
        penalizer.penalize(experiences);
        var state = StateGrid.of(1, 0);
        var action = ActionGrid.N;

        // Verify that the memory has been updated correctly
        var sa = StateActionGrid.of(state, action);
        var agent = (AgentQLearningGrid) dependencies.agent();
        var memory = agent.getMemory();
        var penalty = dependencies.trainerParameters().penaltyActionCorrection();
        assertEquals(penalty, memory.read(sa));
    }

}
