package chapters.ch3;

import chapters.ch3.implem.splitting_path_problem.*;
import core.foundation.gadget.set.SetUtils;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestSplittingPathPolicyRandom {

    EnvironmentSplittingPath environment;
    EnvironmentParametersSplitting parameters;
    SplittingPathPolicyI policy;

    @BeforeEach
    void init() {
        parameters = EnvironmentParametersSplittingFactory.produce();
        environment = EnvironmentSplittingPath.of(parameters);
        policy= SplittingPathPolicyRandom.of(parameters);
    }

    @Test
    void givenRandomPosExceptSplit_whenStepping_thenCorrect() {
        var stateSet=parameters.getStatesExceptSplit();
        var anyFromSet= SetUtils.getAnyFromSet(stateSet);
        var sNext = getsNext(anyFromSet);
        var sNextExpected = StateGrid.of(anyFromSet.x()+1,anyFromSet.y());
        assertEquals(sNextExpected, sNext);
    }

    @Test
    void givenPosSplit_whenStepping_thenCorrect() {
        var sNext = getsNext(StateGrid.of(2,1));
        var possibleNextStates = Set.of(StateGrid.of(2,2), StateGrid.of(2,0));
        assertTrue(possibleNextStates.contains(sNext));
    }


    private StateGrid getsNext(StateGrid state) {
        var sr= environment.step(state, policy.chooseAction(state));
        return sr.sNext();
    }

}
