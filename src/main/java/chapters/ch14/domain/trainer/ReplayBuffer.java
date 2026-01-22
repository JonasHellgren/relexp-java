package chapters.ch14.domain.trainer;

import chapters.ch14.domain.environment.Experience;
import chapters.ch14.domain.settings.TrainerSettings;
import core.foundation.util.rand.RandUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This class represents a replay buffer to store experiences.
 * A replay buffer is a collection of experiences that are
 * used to update the memory.
 * Presetting buffer size speeds up training.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReplayBuffer<SI,S,A> {

    TrainerSettings trainerParameters;
    List<Experience<S,A>> buffer;

    public static <SI,S,A> ReplayBuffer<SI,S,A> of(TrainerSettings trainerParameters) {
        return new ReplayBuffer<>(trainerParameters, createExperienceListWithPresetSize(trainerParameters));
    }

    private static <S, A> @NotNull ArrayList<Experience<S, A>> createExperienceListWithPresetSize(
            TrainerSettings trainerParameters) {
        return new ArrayList<>(trainerParameters.maxSizeReplayBuffer());
    }

    public int size() {
        return buffer.size();
    }

    /**
     * Returns a list of experiences selected at random from the replay buffer.
     * The number of experiences returned is determined by the trainer settings.
     *
     * @return A list of experiences sampled from the replay buffer.
     */
    public List<Experience<S,A>> sampleMiniBatch() {
        int minSize = Math.min(buffer.size(), trainerParameters.sizeMiniBatch());
        return IntStream.range(0, minSize)
                .mapToObj(i -> buffer.get(getRandomIndexInBuffer()))
                .toList();
    }

    /**
     * Adds an experience to the replay buffer.
     *
     * @param e The experience to add to the replay buffer.
     */
    public void add(Experience<S,A> e) {
        buffer.add(e);
    }

    /**
     * Deletes random old experience in the replay buffer if the buffer is full.
     */
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
