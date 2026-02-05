package chapters.ch14.implem.pong_memory;

import chapters.ch14.domain.environment.EnvironmentI;
import chapters.ch14.domain.environment.Experience;
import chapters.ch14.domain.long_memory.LongMemory;
import chapters.ch14.domain.settings.TrainerSettings;
import chapters.ch14.domain.trainer.MiniBatchAdapterI;
import chapters.ch14.implem.pong.ActionPong;
import chapters.ch14.implem.pong.PongSettings;
import chapters.ch14.implem.pong.StateLongPong;
import chapters.ch14.implem.pong.StatePong;
import core.foundation.gadget.training.TrainDataOld;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;


/**
 * This class represents a mini-batch adapter for the Pong environment.
 * It adapts a list of experiences into a train data object.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MiniBatchAdapterPong implements MiniBatchAdapterI<StateLongPong, StatePong, ActionPong> {

    private final PongSettings settings;
    private final EnvironmentI<StatePong, ActionPong> environment;
    private final LongMemory<StateLongPong> longMemory;
    private final TrainerSettings trainerSettings;

    public static MiniBatchAdapterPong of(PongSettings settings,
                                          EnvironmentI<StatePong, ActionPong> environment,
                                          LongMemory<StateLongPong> longMemory,
                                          TrainerSettings trainerSettings
    ) {
        return new MiniBatchAdapterPong(settings, environment, longMemory, trainerSettings);
    }


    /**
     * Adapts a list of experiences into a train data object.
     * @param experiences The list of experiences to adapt.
     * @return The adapted train data object.
     */
    @Override
    public TrainDataOld adapt(List<Experience<StatePong, ActionPong>> experiences) {
        var timeHitCalculator = BallHitFloorCalculator.of(environment, settings);
        var buffer = TrainDataOld.emptyFromOutputs();
        for (Experience<StatePong, ActionPong> exp : experiences) {
            var sl = StateAdapterPong.stateLong(timeHitCalculator, exp.state());
            var slNew = StateAdapterPong.stateLong(timeHitCalculator, exp.stateNew());
            double value = exp.isTerminal()
                    ? exp.reward()
                    : exp.reward() + trainerSettings.gamma() * longMemory.read(slNew);
            var in = StateAdapterPong.asInput(sl);
            buffer.addIAndOut(in, value);
        }
        return buffer;
    }

}
