package chapters.ch14.implem.pong_memory;

import chapters.ch14.domain.interfaces.LongMemory;
import chapters.ch14.implem.pong.StateLongPong;
import core.foundation.gadget.training.TrainDataOld;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * This class represents a zero-initialized LongMemory.
 *
 * It implements the LongMemory interface and provides default implementations for the read, write, and fit methods.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LongMemoryZero implements LongMemory<StateLongPong> {

    public static LongMemoryZero create() {
        return new LongMemoryZero();
    }

    @Override
    public double read(StateLongPong state) {
        return 0;
    }

    @Override
    public void write(StateLongPong state, double value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fit(TrainDataOld data) {
        throw new UnsupportedOperationException();

    }
}
