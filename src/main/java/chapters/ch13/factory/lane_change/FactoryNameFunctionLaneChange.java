package chapters.ch13.factory.lane_change;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.implem.lane_change.ActionLane;
import chapters.ch13.implem.lane_change.StateLane;
import core.foundation.util.rand.RandUtil;
import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class FactoryNameFunctionLaneChange {


    public Function<Experience<StateLane, ActionLane>, String> rand5DigitLane = (e) ->
            String.valueOf(RandUtil.getRandomIntNumber(0,99999));

}
