package chapters.ch12.domain.inv_pendulum.environment.core;

import lombok.Builder;


@Builder
public record StepReturnPendulum(
        StatePendulum stateNew,
        boolean isFail,
        boolean isTerminal,
        double reward
) {

}
