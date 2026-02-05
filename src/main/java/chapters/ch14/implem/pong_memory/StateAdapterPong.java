package chapters.ch14.implem.pong_memory;

import chapters.ch14.implem.pong.StateLongPong;
import chapters.ch14.implem.pong.StatePong;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;
import java.util.List;

/**
 * This class provides utility methods for adapting long memory state objects in the Pong game.
 */
@UtilityClass
public class StateAdapterPong {

    public static List<Double> asInput(StateLongPong state) {
        return List.of(state.getTimeHit(),state.getDeltaX());
    }

    public static List<Double> asInput(Pair<Double, Double> pair) {
        return List.of(pair.getFirst(),pair.getSecond());
    }

    public static StateLongPong stateLong(BallHitFloorCalculator timeHitCalculator, StatePong state) {
        var hitRes = timeHitCalculator.calculate(state.copy());
        double deltaX = hitRes.xBall()-state.posPaddle().x();
        return StateLongPong.of(hitRes.timeHit(), Math.abs(deltaX));
    }

}
