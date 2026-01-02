package chapters.ch6.domain.trainer.multisteps_after_episode;

import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.Builder;
import java.util.Optional;

@Builder
public record MultiStepResultGrid (
        StateGrid state,          // Initial state
        ActionGrid action,            // Action taken
        double sumRewards,        // Discounted sum of rewards
        Optional<StateGrid> stateFuture,   // Future state, may be empty if outside n-step horizon
        Optional<ActionGrid> actionFuture // Future action, may be empty if outside n-step horizon
) {

    public boolean isStateFuturePresent() {
        return stateFuture.isPresent();
    }

}
