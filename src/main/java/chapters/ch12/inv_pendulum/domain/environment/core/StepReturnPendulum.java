package chapters.ch12.inv_pendulum.domain.environment.core;

import lombok.Builder;


@Builder
public record StepReturnPendulum(
        StatePendulum stateNew,
        boolean isFail,
        boolean isTerminal,
        double reward
) {

}
