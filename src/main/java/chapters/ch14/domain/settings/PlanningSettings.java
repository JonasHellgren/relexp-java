package chapters.ch14.domain.settings;

import lombok.Builder;
import lombok.With;

/***
 * If isAllRolloutsFailed is true after nRolloutsMin, then new try with timeMaxMs
 */
@Builder
@With
public record PlanningSettings(
        int maxTimeInMs,
        int minNofIterations,
        int maxDepth,
        double probActionChange
) {
}
