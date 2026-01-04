package chapters.ch14.domain.settings;

import lombok.Builder;
import lombok.With;

/**
 * This record represents the settings for a trainer.
 * It contains the parameters that control the behavior of the trainer.
 */
@Builder
@With
public record TrainerSettings(
        int sizeMiniBatch,
        int nFits,
        int maxSizeReplayBuffer,
        int maxEpisodes,
        int maxStepsPerEpisode,
        double gamma,
        boolean isShowLogging
) {
}
