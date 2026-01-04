package chapters.ch13.domain.searcher.settings;

import lombok.Builder;
import lombok.With;

/**
 * Represents the settings for a searcher in the MCTS algorithm.
 * This record contains the parameters that control the behavior of the searcher.
 */
@Builder
@With
public record SearcherSettings(
        double uctExploration,
        int maxTreeDepth,
        int maxDepth,
        int maxIterations,
        double discountNormal,
        double discountDefensive,
        double learningRateNormal,
        double learningRateDefensive
) {
}
