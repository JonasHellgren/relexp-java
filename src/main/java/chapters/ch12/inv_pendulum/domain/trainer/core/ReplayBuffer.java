package chapters.ch12.inv_pendulum.domain.trainer.core;

import chapters.ch12.inv_pendulum.domain.trainer.param.TrainerParameters;
import core.foundation.util.rand.RandUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents a replay buffer used in reinforcement learning to store experiences.
 * A replay buffer is a collection of experiences that are used to update the model's parameters.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplayBuffer {

    TrainerParameters trainerParameters;
    List<ExperiencePendulum> buffer;

    public static ReplayBuffer of(TrainerDependencies dependencies) {
        return new ReplayBuffer(dependencies.trainerParameters(), new ArrayList<>());
    }

    public int size() {
        return buffer.size();
    }

    public MiniBatch sampleMiniBatch() {
        int minSize = Math.min(buffer.size(), trainerParameters.sizeMiniBatch());
        var expList = IntStream.range(0, minSize)
                .mapToObj(i -> buffer.get(getRandomIndexInBuffer()))
                .toList();
        return MiniBatch.of(expList);
    }

    public void add(ExperiencePendulum e) {
        buffer.add(e);
    }

    public void maybeDeleteOldExperience() {
        if (isFull()) {
            int idxToDelete = getRandomIndexInBuffer();
            buffer.remove(idxToDelete);
        }
    }

    public boolean isFull() {
        return buffer.size() > trainerParameters.maxSizeReplayBuffer();
    }

    private int getRandomIndexInBuffer() {
        return RandUtil.getRandomIntNumber(0, buffer.size());
    }

}
