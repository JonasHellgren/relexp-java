package chapters.ch11.domain.trainer.multisteps;

import chapters.ch11.domain.environment.core.StateLunar;
import lombok.Builder;
import java.util.Optional;

/**
 * Represents a single step in a multi-step experience.
 */
@Builder
public record MultiStepResult(
        StateLunar state,          // Initial state
        double action,             // Action taken
        Double sumRewards,         // Sum of rewards
        Optional<StateLunar> stateFuture,    // Future state, may be empty if outside n-step horizon
        Double valueTarget,        // Target value for training
        Double advantage,          // Advantage for training
        double tdError            // TD error for training
) {

    public boolean isStateFutureTerminalOrNotPresent() {
        return stateFuture.isEmpty();
    }

}
