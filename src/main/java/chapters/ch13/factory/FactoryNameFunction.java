package chapters.ch13.factory;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.environments.jumper.*;
import chapters.ch13.environments.lane_change.*;
import core.foundation.util.rand.RandUtil;
import lombok.experimental.UtilityClass;
import java.util.function.Function;

@UtilityClass
public class FactoryNameFunction {


    public Function<Experience<StateJumper, ActionJumper>, String> rand3DigitClimber = (e) ->
         String.valueOf(RandUtil.getRandomIntNumber(0,999));


    public Function<Experience<StateLane, ActionLane>, String> rand5DigitLane = (e) ->
         String.valueOf(RandUtil.getRandomIntNumber(0,99999));


}
