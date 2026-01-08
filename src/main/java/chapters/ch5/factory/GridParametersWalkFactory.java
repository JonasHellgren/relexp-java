package chapters.ch5.factory;

import chapters.ch5.implem.walk.RandomWalkParameters;
import chapters.ch5.implem.walk.StateWalk;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class GridParametersWalkFactory {

    public static RandomWalkParameters produce() {
        return RandomWalkParameters.builder()
                .terminalNonFailsStates(Set.of(StateWalk.of(5)))
                .failStates(Set.of(StateWalk.of(1)))
                .xMax(1).xMax(5)
                .build();
    }

}
