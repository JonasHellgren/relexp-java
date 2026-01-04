package chapters.ch5.implem.walk;


import chapters.ch5.domain.environment.StartStateSupplierI;
import chapters.ch5.domain.environment.StateMcI;
import core.foundation.util.collections.MyListUtils;
import core.foundation.util.rand.RandUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static chapters.ch5.implem.walk.RandomWalkParameters.NON_TERMINAL_POSITIONS;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartStateSupplierWalk implements StartStateSupplierI {

    public static StartStateSupplierWalk create() {
        return new StartStateSupplierWalk();
    }

    @Override
    public String environmentName() {
        return EnvironmentWalk.NAME;
    }

    @Override
    public StateMcI getStartState() {
        return StateWalk.of(RandUtils.getRandomIntNumber(
                MyListUtils.findMinInt(NON_TERMINAL_POSITIONS).orElseThrow(),
                MyListUtils.findMaxInt(NON_TERMINAL_POSITIONS).orElseThrow()));
    }
}
