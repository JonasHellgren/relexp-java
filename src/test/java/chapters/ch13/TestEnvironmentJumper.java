package chapters.ch13;

import chapters.ch13.domain.environment.EnvironmentI;
import chapters.ch13.domain.environment.StepReturnI;
import chapters.ch13.environments.jumper.ActionJumper;
import chapters.ch13.environments.jumper.EnvironmentJumper;
import chapters.ch13.environments.jumper.StateJumper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestEnvironmentJumper {

    EnvironmentI<StateJumper, ActionJumper> environment;

    @BeforeEach
    void init() {
         environment = EnvironmentJumper.create();
    }

    @Test
    void testStep_UpAction_IncreasesHeight() {
        StateJumper state = StateJumper.ofHeight(0);
        ActionJumper action = ActionJumper.up;
        StepReturnI<StateJumper> result = environment.step(state, action);

        assertEquals(1, result.stateNew().height());
        assertFalse(result.isFail());
        assertFalse(result.isTerminal());
        assertEquals(EnvironmentJumper.REWARD_COIN, result.reward());
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
                EnvironmentJumper.REWARD_COIN + EnvironmentJumper.REWARD_FAIL,
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
        assertEquals(EnvironmentJumper.REWARD_NOT_UP, result.reward());
    }

    @Test
    void testStep_UpActionAtMaxHeight_TerminalAndCoinReward() {
        StateJumper state = StateJumper.of(EnvironmentJumper.MAX_HEIGHT - 1, EnvironmentJumper.MAX_HEIGHT - 1);
        ActionJumper action = ActionJumper.up;
        StepReturnI<StateJumper> result = environment.step(state, action);

        assertEquals(EnvironmentJumper.MAX_HEIGHT, result.stateNew().height());
        assertFalse(result.isFail());
        assertTrue(result.isTerminal());
        assertEquals(EnvironmentJumper.REWARD_COIN, result.reward());
    }

}
