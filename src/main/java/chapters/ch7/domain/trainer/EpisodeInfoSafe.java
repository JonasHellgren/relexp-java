package chapters.ch7.domain.trainer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Represents information about an step in a learning process.
 * An step is a sequence of experiences.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EpisodeInfoSafe {
    List<ExperienceGridCorrectedAction> experiences;

    public static EpisodeInfoSafe of(List<ExperienceGridCorrectedAction> experiences) {
        return new EpisodeInfoSafe(experiences);
    }

    public Double sumRewards() {
        return experiences.stream().mapToDouble(e -> e.experienceGrid().reward()).sum();
    }

    public Integer nSteps() {
        return experiences.size();
    }
}
