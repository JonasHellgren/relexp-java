package chapters.ch13.factory.lane_change;

import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.StateLane;
import core.foundation.util.rand.RandUtil;
import lombok.experimental.UtilityClass;
import java.util.function.Function;

@UtilityClass
public class FactoryRolloutPolicyLaneChange {

    public Function<StateLane, ActionLane> random = state -> {
        int idx= RandUtil.getRandomIntNumber(0, ActionLane.actions().size());
        return ActionLane.actions().get(idx);
    };


    public static final double PROB_ZERO = 0.8;
    public Function<StateLane, ActionLane> zeroOrRandom = state -> {
        double r= RandUtil.randomNumberBetweenZeroAndOne();
        return (r< PROB_ZERO)
                ? ActionLane.ZERO
                : random.apply(state);
    };

}
