package chapters.ch4;

import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import chapters.ch4.implem.blocked_road_lane.factory.FactoryEnvironmentParametersRoad;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class TestEnvironmentRoad {

    private EnvironmentRoad environment;
    private EnvironmentGridParametersI parameters;
    StateGrid state00;
    ActionGrid actionE, actionN;


    @BeforeEach
    void setUp() {
        parameters = FactoryEnvironmentParametersRoad.produceRoadFixedFailReward();
        environment = EnvironmentRoad.of(parameters);
        state00 = StateGrid.of(0,0);
        actionE = ActionGrid.E;
        actionN = ActionGrid.N;
    }

    @Test
    void testStep_validStateAndAction_returnsValidStepReturnGrid() {
        StepReturnGrid stepReturnGrid = environment.step(state00, actionE);
        assertNotNull(stepReturnGrid);
        assertNotNull(stepReturnGrid.sNext());
        assertFalse(stepReturnGrid.isFail());
        assertFalse(stepReturnGrid.isTerminal());
    }

    @Test
    void whenSteppingEast_thenXIncreases() {
        var sr = environment.step(state00, actionE);
        assertEquals(1, sr.sNext().x());
        assertEquals(0, sr.sNext().y());
    }

    @Test
    void whenSteppingNorth_thenXandYIncreases() {
        var sr = environment.step(state00, actionN);
        assertEquals(1, sr.sNext().x());
        assertEquals(1, sr.sNext().y());
    }

    @Test
    void moveToTerminalState_thenCorrect() {
        StateGrid state = StateGrid.of(2, 0);
        var sr = environment.step(state, actionE);
        assertEquals(0,sr.reward());  //not moving N or S
        assertTrue(sr.isTerminal());
    }

    @Test
    void moveToFailState_thenCorrect() {
        StateGrid state = StateGrid.of(2, 1);
        var sr = environment.step(state, actionE);
        assertEquals(parameters.rewardAtTerminalPos(StateGrid.of(3, 1)),sr.reward());
        assertTrue(sr.isTerminal());
        assertTrue(sr.isFail());
    }

    @Test
    void moveNorthInUpper_thenCorrect() {
        StateGrid state = StateGrid.of(1, 1);
        var sr = environment.step(state, actionN);
        assertEquals(parameters.rewardMove(),sr.reward());
        assertFalse(sr.isTerminal());
        assertEquals(StateGrid.of(2,1),sr.sNext());
    }


}
