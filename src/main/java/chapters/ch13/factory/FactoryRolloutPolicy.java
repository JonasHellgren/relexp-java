package chapters.ch13.factory;

import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.StateJumper;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.StateLane;
import core.foundation.util.rand.RandUtil;
import lombok.experimental.UtilityClass;
import java.util.function.Function;

@UtilityClass
public class FactoryRolloutPolicy {

   public Function<StateJumper, ActionJumper> rolloutPolicyClimb = state -> {
        int idx= RandUtil.getRandomIntNumber(0, ActionJumper.actions().size());
        return ActionJumper.actions().get(idx);
    };

    public Function<StateLane, ActionLane> rolloutPolicyLaneRandom = state -> {
        int idx= RandUtil.getRandomIntNumber(0, ActionLane.actions().size());
        return ActionLane.actions().get(idx);
    };


    public static final double PROB_ZERO = 0.8;
    public Function<StateLane, ActionLane> rolloutPolicyLaneZeroOrRandom = state -> {
        double r= RandUtil.randomNumberBetweenZeroAndOne();
        return (r< PROB_ZERO)
                ? ActionLane.ZERO
                : rolloutPolicyLaneRandom.apply(state);
    };

}
