package chapters.ch13;

import chapters.ch13.domain.environment.StepReturnI;
import chapters.ch13.environments.lane_change.ActionLane;
import chapters.ch13.environments.lane_change.EnvironmentLane;
import chapters.ch13.environments.lane_change.StateLane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestEnvironmentLane {

    private EnvironmentLane environment;
    private StateLane state0;

    @BeforeEach
    void setup() {
        environment = EnvironmentLane.create();
        state0 = StateLane.of(0, 0, 0, 0);

    }

    @Test
    void testNextState() {
        var action = ActionLane.POS; // Assuming there's at least one action
        StateLane stateNew = environment.step(state0, action).stateNew();
        assertNotEquals(state0, stateNew);
        assertTrue(stateNew.time() > 0);
    }

    @Test
    void testIsFail() {
        var sr = environment.step(state0, ActionLane.POS);  //direct heading change, delayed y pos change
        sr = environment.step(sr.stateNew(), ActionLane.ZERO);
        assertTrue(sr.isFail());
    }

    @Test
    void testIsTerminalState() {
        var state = StateLane.ofWithTime(0, 0, 0, 0, 10);
        var action = ActionLane.POS;
        boolean isTerminal = environment.step(state, action).isTerminal();
        assertTrue(isTerminal);
    }

    @Test
    void testCalculateReward() {
        var action = ActionLane.NEG;
        double reward = environment.step(state0, action).reward();
        assertEquals(environment.getSettings().rChangeSteering(), reward);
    }

    @Test
    void testLaneChange() {
        List<ActionLane> actions = List.of(
                ActionLane.NEG, ActionLane.NEG, ActionLane.NEG,
                ActionLane.POS, ActionLane.POS, ActionLane.POS,
                ActionLane.ZERO);

        var s = state0.copy();
        var sr = manySteps(actions, s);
        var settings = environment.getSettings();
        Assertions.assertTrue(sr.stateNew().y() < 0);
        assertEquals(settings.rCorrectYPos()+settings.rChangeSteering()
                , sr.reward(), 0.01);
    }

    @Test
    void testDitch() {
        List<ActionLane> actions = List.of(
                ActionLane.NEG, ActionLane.NEG, ActionLane.NEG,
                ActionLane.ZERO, ActionLane.ZERO, ActionLane.ZERO, ActionLane.ZERO, ActionLane.ZERO);

        var s = state0.copy();
        var sr = manySteps(actions, s);
        var settings = environment.getSettings();
        Assertions.assertTrue(sr.stateNew().y() < settings.yPosDitch());
        assertEquals(settings.rFail(), sr.reward(), 0.01);
    }

    private StepReturnI<StateLane> manySteps(List<ActionLane> actions,
                                             StateLane s) {
        StepReturnI<StateLane> sr = null;
        for (ActionLane action : actions) {
            sr = environment.step(s, action);
            s = sr.stateNew().copy();
        }
        return sr;
    }

}



