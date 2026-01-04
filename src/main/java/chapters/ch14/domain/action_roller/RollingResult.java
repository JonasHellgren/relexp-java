package chapters.ch14.domain.action_roller;

import lombok.Builder;

/**
 * This class represents the result of a rolling operation in the action roller domain.
 * It holds the accumulated rewards, a flag indicating whether the rolling operation is terminal,
 * a flag indicating whether the rolling operation failed, and the number of steps taken.
 */
@Builder
public record RollingResult(
        double accRewards,
        boolean isEndTerminal,
        boolean isEndFail,
        int nSteps
) {
}
