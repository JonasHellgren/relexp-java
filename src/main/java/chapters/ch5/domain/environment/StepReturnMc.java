package chapters.ch5.domain.environment;

import lombok.Builder;

@Builder
public record StepReturnMc(
        StateMcI sNext,
        boolean isFail,
        boolean isTerminal,
        double reward
)
{
}
