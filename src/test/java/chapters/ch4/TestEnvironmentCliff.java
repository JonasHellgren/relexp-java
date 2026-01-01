package chapters.ch4;

import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import chapters.ch4.implem.cliff_walk.factory.FactoryEnvironmentParametersCliff;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestEnvironmentCliff {

    private EnvironmentCliff environment;
    private EnvironmentGridParametersI parameters;
    StateGrid state00;
    ActionGrid actionN,actionE, actionS, actionW;


    @BeforeEach
    void setUp() {
        parameters = FactoryEnvironmentParametersCliff.produceCliff();
        environment = EnvironmentCliff.of(parameters);
        state00 = StateGrid.of(0,0);
        actionN = ActionGrid.N;
        actionE = ActionGrid.E;
        actionS = ActionGrid.S;
        actionW = ActionGrid.W;
    }

    @Test
    void givenPos00_testStep_validStateAndAction_returnsValidStepReturnGrid() {
        StepReturnGrid stepReturnGrid = environment.step(state00, actionN);
        assertNotNull(stepReturnGrid);
        assertNotNull(stepReturnGrid.sNext());
        assertFalse(stepReturnGrid.isFail());
        assertFalse(stepReturnGrid.isTerminal());
    }

    @Test
    void givenPos00_whenSteppingNorth_thenYIncreases() {
        var sr = environment.step(state00, actionN);
        assertEquals(StateGrid.of(0, 1), sr.sNext());
    }

    @Test
    void givenPos00_whenSteppingWest_thenXandYIncreases() {
        var sr = environment.step(state00, actionW);
        assertEquals(StateGrid.of(0, 0), sr.sNext());
    }

    @Test
    void givenPos00_moveToFailState_thenCorrect() {
        var sr = environment.step(state00, actionE);
        var rFail=parameters.rewardAtTerminalPos(StateGrid.of(1, 0));
        double rMove=parameters.rewardMove();
        assertEquals(rFail+rMove,sr.reward());
        assertTrue(sr.isTerminal());
        assertTrue(sr.isFail());
    }

    @Test
    void moveToGoalState_thenCorrect() {
        StateGrid state = StateGrid.of(10, 1);
        var sr = environment.step(state, actionS);
        var rGoal=parameters.rewardAtTerminalPos(StateGrid.of(10, 0));
        double rMove=parameters.rewardMove();
        assertEquals(rGoal+rMove,sr.reward());
        assertTrue(sr.isTerminal());
        assertFalse(sr.isFail());
    }

    @Test
    void moveNorthInUpper_thenSameNextPos() {
        StateGrid state = StateGrid.of(1, parameters.getPosYMinMax().getSecond());
        var sr = environment.step(state, actionN);
        assertEquals(parameters.rewardMove(),sr.reward());
        assertFalse(sr.isTerminal());
        assertEquals(state,sr.sNext());
    }

}
