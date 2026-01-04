package chapters.ch14;

import chapters.ch14.domain.environment.Experience;
import chapters.ch14.domain.environment.StepReturn;
import chapters.ch14.domain.trainer.ReplayBuffer;
import chapters.ch14.domain.trainer.TrainerDependencies;
import chapters.ch14.environments.pong.ActionPong;
import chapters.ch14.environments.pong.StateLongPong;
import chapters.ch14.environments.pong.StatePong;
import chapters.ch14.factory.FactoryDependencies;
import chapters.ch14.factory.FactoryTrainerSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestReplayBuffer {


    public static final Experience<StatePong, ActionPong> EXP =
            Experience.of(StatePong.empty(), ActionPong.still, StepReturn.empty());
    ReplayBuffer<StateLongPong, StatePong, ActionPong> replayBuffer;
    TrainerDependencies<StateLongPong, StatePong, ActionPong> dependencies;

    @BeforeEach
    void init() {
        dependencies = FactoryDependencies.forTest(FactoryTrainerSettings.forTest());
        replayBuffer = ReplayBuffer.of(dependencies.trainerSettings());
    }

    @Test
    void testSize() {
        assertEquals(0, replayBuffer.size());
        replayBuffer.add(EXP);
        assertEquals(1, replayBuffer.size());
    }

    @Test
    void testSampleMiniBatch() {
        IntStream.range(0, 20).forEach(i -> replayBuffer.add(EXP));
        var miniBatch = replayBuffer.sampleMiniBatch();
        assertEquals(dependencies.trainerSettings().sizeMiniBatch(), miniBatch.size());
        assertTrue(miniBatch.contains(replayBuffer.getBuffer().get(0)));
        assertTrue(miniBatch.contains(replayBuffer.getBuffer().get(3)));
    }

    @Test
    void testAdd() {
        replayBuffer.add(EXP);
        assertTrue(replayBuffer.getBuffer().contains(EXP));
    }

}
