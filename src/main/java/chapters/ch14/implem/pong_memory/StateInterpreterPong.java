package chapters.ch14.implem.pong_memory;

import chapters.ch14.domain.state_intepreter.StateInterpreterI;
import chapters.ch14.implem.pong.StateLongPong;
import chapters.ch14.implem.pong.StatePong;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StateInterpreterPong<SI, S> implements StateInterpreterI<StateLongPong, StatePong> {

    BallHitFloorCalculator timeHitCalculator;

    public static <SI, S> StateInterpreterPong<SI, S> create(BallHitFloorCalculator timeHitCalculator) {
        return new StateInterpreterPong<>(timeHitCalculator);
    }

    @Override
    public StateLongPong interpret(StatePong statePong) {
        return  StateAdapterPong.stateLong(timeHitCalculator, statePong);
    }
}
