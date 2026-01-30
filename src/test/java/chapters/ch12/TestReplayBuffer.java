package chapters.ch12;

import chapters.ch12.domain.inv_pendulum.environment.core.ActionPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StepReturnPendulum;
import chapters.ch12.domain.inv_pendulum.trainer.core.ExperiencePendulum;
import chapters.ch12.domain.inv_pendulum.trainer.core.ReplayBuffer;
import chapters.ch12.domain.inv_pendulum.trainer.core.TrainerDependencies;
import chapters.ch12.factory.TrainerDependenciesFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestReplayBuffer {

    static final StepReturnPendulum STEP_RETURN = StepReturnPendulum.builder()
            .stateNew(StatePendulum.ofStart(0.1d, 0d)).isFail(false).isTerminal(false).reward(0d).build();
    static final ExperiencePendulum EXPER =
            ExperiencePendulum.of(StatePendulum.ofStart(0d, 0d), ActionPendulum.N, STEP_RETURN);
    private ReplayBuffer replayBuffer;
    TrainerDependencies dependencies;

    @BeforeEach
    void init() {
        dependencies = TrainerDependenciesFactory.createForTest();
        replayBuffer = ReplayBuffer.of(dependencies);
    }

    @Test
    void testSize() {
        assertEquals(0, replayBuffer.size());
        replayBuffer.add(EXPER); // Add an experience
        assertEquals(1, replayBuffer.size());
    }

    @Test
    void testSampleMiniBatch() {
        for (int i = 0; i < 100; i++) {
            replayBuffer.add(EXPER);
        }

        var miniBatch = replayBuffer.sampleMiniBatch();
        assertNotNull(miniBatch);
        assertEquals(dependencies.trainerParameters().sizeMiniBatch(), miniBatch.size());
    }

    @Test
    void testAdd() {
        replayBuffer.add(EXPER);
        assertTrue(replayBuffer.sampleMiniBatch().contains(EXPER));
    }

    @Test
    void testMaybeDeleteOldExperience() {
        // Add maxSizeReplayBuffer()+1 experiences
        for (int i = 0; i < dependencies.trainerParameters().maxSizeReplayBuffer() + 1; i++) {
            replayBuffer.add(EXPER);
        }
        replayBuffer.maybeDeleteOldExperience();
        assertEquals(dependencies.trainerParameters().maxSizeReplayBuffer(), replayBuffer.size());
    }


}
