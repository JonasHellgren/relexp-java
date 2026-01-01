package chapters.ch2;

import _shared.grid.core.StateGrid;
import a_concepts.splitting_path_problem.EnvironmentParametersSplittingFactory;
import a_concepts.splitting_path_problem.EnvironmentSplittingPath;
import a_concepts.splitting_path_problem.SplittingPathPolicyI;
import a_concepts.splitting_path_problem.SplittingPathPolicyOptimal;
import org.hellgren.utilities.set_utlis.SetUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TestSplittingPathPolicyOptimal {

    EnvironmentSplittingPath environment;
    SplittingPathPolicyI policy;

    @BeforeEach
    void init() {
        var parameters = EnvironmentParametersSplittingFactory.produce();
        environment = EnvironmentSplittingPath.of(parameters);
        policy= SplittingPathPolicyOptimal.of(parameters);
    }

    @Test
    void givenRandomPosExceptSplit_whenStepping_thenCorrect() {
        var stateSet=Set.of(StateGrid.of(0,1),StateGrid.of(1,1),
                StateGrid.of(2,2),StateGrid.of(3,2),StateGrid.of(4,2),
                StateGrid.of(2,0),StateGrid.of(3,0),StateGrid.of(4,0));
        var anyFromSet= SetUtils.getAnyFromSet(stateSet);
        var sNext = getsNext(anyFromSet);
        var sNextExpected =StateGrid.of(anyFromSet.x()+1,anyFromSet.y());
        assertEquals(sNextExpected, sNext);
    }

    @Test
    void givenPosSplitPos_whenStepping_thenCorrect() {
        var sNext = getsNext(StateGrid.of(2,1));
        assertEquals(StateGrid.of(2,2), sNext);
    }


    private StateGrid getsNext(StateGrid state) {
        var sr= environment.step(state, policy.chooseAction(state));
        return sr.sNext();
    }

}
