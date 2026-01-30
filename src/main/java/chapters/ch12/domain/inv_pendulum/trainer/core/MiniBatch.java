package chapters.ch12.domain.inv_pendulum.trainer.core;

import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a mini-batch of experiences used for training in reinforcement learning.
 * A mini-batch is a collection of experiences that are used to update the model's parameters.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MiniBatch  implements Iterable<ExperiencePendulum> {

    List<ExperiencePendulum> buffer;

    public static MiniBatch of(List<ExperiencePendulum> buffer) {
        return new MiniBatch(buffer);
    }

    public int size() {
        return buffer.size();
    }

    public List<StatePendulum> getStateList() {
        return buffer.stream().map(ExperiencePendulum::state).toList();
    }

    @NotNull
    @Override
    public Iterator<ExperiencePendulum> iterator() {
        return buffer.iterator();
    }

    public boolean contains(ExperiencePendulum exper) {
        return buffer.contains(exper);
    }
}
