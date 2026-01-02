package chapters.ch6;

import chapters.ch6.domain.trainer.multisteps_after_episode.MultiStepResultGrid;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TestMultiStepResults {

    @Test
     void testBuilder() {
        StateGrid state = StateGrid.of(0,0); // Assuming StateGrid has a no-arg constructor
        var action = ActionGrid.E;
        double sumRewards = 2.0;
        Optional<StateGrid> stateFuture = Optional.of(StateGrid.of(1,0));
        MultiStepResultGrid result = MultiStepResultGrid.builder()
                .state(state)
                .action(ActionGrid.E)
                .sumRewards(sumRewards)
                .stateFuture(stateFuture)
                .build();

        assertEquals(state, result.state());
        assertEquals(action, result.action());
        assertEquals(sumRewards, result.sumRewards());
        assertEquals(stateFuture, result.stateFuture());
    }

    @Test
     void testBuilderWithEmptyStateFuture() {
        StateGrid state = StateGrid.of(0,0); // Assuming StateGrid has a no-arg constructor
        var action = ActionGrid.E;
        double sumRewards = 2.0;

        MultiStepResultGrid result = MultiStepResultGrid.builder()
                .state(state)
                .action(ActionGrid.E)
                .sumRewards(sumRewards)
                .stateFuture(Optional.empty())
                .build();

        assertEquals(state, result.state());
        assertEquals(action, result.action());
        assertEquals(sumRewards, result.sumRewards());
        assertFalse(result.isStateFuturePresent());
    }

}
