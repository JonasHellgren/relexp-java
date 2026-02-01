package chapters.ch13.factory.jumper;

import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.StateJumper;
import core.foundation.util.rand.RandUtil;
import lombok.experimental.UtilityClass;
import java.util.function.Function;

@UtilityClass
public class FactoryRolloutPolicyJumper {


    public Function<StateJumper, ActionJumper> rolloutPolicy = state -> {
        int idx= RandUtil.getRandomIntNumber(0, ActionJumper.actions().size());
        return ActionJumper.actions().get(idx);
    };


}
