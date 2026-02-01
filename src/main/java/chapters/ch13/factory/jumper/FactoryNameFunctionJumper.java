package chapters.ch13.factory.jumper;

import chapters.ch13.domain.environment.Experience;
import chapters.ch13.implem.jumper.*;
import core.foundation.util.rand.RandUtil;
import lombok.experimental.UtilityClass;
import java.util.function.Function;

@UtilityClass
public class FactoryNameFunctionJumper {

    public Function<Experience<StateJumper, ActionJumper>, String> rand3DigitClimber = (e) ->
         String.valueOf(RandUtil.getRandomIntNumber(0,999));



}
