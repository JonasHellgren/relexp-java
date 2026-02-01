package chapters.ch13;

import chapters.ch13.domain.environment.EnvironmentI;
import chapters.ch13.domain.environment.StepReturnI;
import chapters.ch13.factory.jumper.JumperParametersFactory;
import chapters.ch13.implem.jumper.ActionJumper;
import chapters.ch13.implem.jumper.EnvironmentJumper;
import chapters.ch13.implem.jumper.JumperParameters;
import chapters.ch13.implem.jumper.StateJumper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestEnvironmentJumper {

    EnvironmentI<StateJumper, ActionJumper> environment;
    JumperParameters parameters;

    @BeforeEach
    void init() {
        parameters = JumperParametersFactory.produce();
        environment = EnvironmentJumper.of(parameters);
    }

    @Test
    void testStep_UpAction_IncreasesHeight() {
        StateJumper state = StateJumper.ofHeight(0);
        ActionJumper action = ActionJumper.up;
        StepReturnI<StateJumper> result = environment.step(state, action);

        assertEquals(1, result.stateNew().height());
        assertFalse(result.isFail());
        assertFalse(result.isTerminal());
        assertEquals(parameters.rewardCoin(), result.reward());
    }

    @Test
    void testStep_NeutralAction_SameHeight() {
        StateJumper state = StateJumper.ofHeight(1);
        ActionJumper action = ActionJumper.n;
        StepReturnI<StateJumper> result = environment.step(state, action);

        assertEquals(1, result.stateNew().height());
        assertTrue(result.isFail());
        assertTrue(result.isTerminal());
        assertEquals(
                parameters.rewardCoin() + parameters.rewardFail(),
                result.reward());
    }

    @Test
    void testStep_NeutralActionFromHeight0() {
        StateJumper state = StateJumper.zeroHeight();
        ActionJumper action = ActionJumper.n;
        StepReturnI<StateJumper> result = environment.step(state, action);

        assertEquals(0, result.stateNew().height());
        assertFalse(result.isFail());
        assertFalse(result.isTerminal());
        assertNotEquals(parameters.rewardFail(), result.reward());
    }

    @Test
    void testStep_UpActionAtMaxHeight_TerminalAndCoinReward() {
        StateJumper state = StateJumper.of(parameters.maxHeight() - 1, parameters.maxHeight() - 1);
        ActionJumper action = ActionJumper.up;
        StepReturnI<StateJumper> result = environment.step(state, action);

        assertEquals(parameters.maxHeight(), result.stateNew().height());
        assertFalse(result.isFail());
        assertTrue(result.isTerminal());
        assertEquals(parameters.rewardCoin(), result.reward());
    }

}
