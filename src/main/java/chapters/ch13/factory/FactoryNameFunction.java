package chapters.ch13.factory;

import k_mcts.domain.environment.Experience;
import k_mcts.environments.jumper.ActionJumper;
import k_mcts.environments.jumper.StateJumper;
import k_mcts.environments.lane_change.ActionLane;
import k_mcts.environments.lane_change.StateLane;
import lombok.experimental.UtilityClass;
import org.hellgren.utilities.random.RandUtils;

import java.util.function.Function;

@UtilityClass
public class FactoryNameFunction {


    public Function<Experience<StateJumper, ActionJumper>, String> rand3DigitClimber = (e) ->
         String.valueOf(RandUtils.getRandomIntNumber(0,999));


    public Function<Experience<StateLane, ActionLane>, String> rand5DigitLane = (e) ->
         String.valueOf(RandUtils.getRandomIntNumber(0,99999));


}
