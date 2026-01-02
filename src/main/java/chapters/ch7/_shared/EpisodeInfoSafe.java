package chapters.ch7._shared;

import chapters.ch7.domain.trainer.ExperienceGridCorrectedAction;
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

    public double fractionOfCorrectSteps() {
        int nCorrected=0;
        for (ExperienceGridCorrectedAction e : experiences) {
            if (e.isCorrected()) {
                nCorrected++;
            }
        }
        return nCorrected / (double) experiences.size();
    }

    public Double sumRewards() {
        return experiences.stream().mapToDouble(e -> e.experienceGrid().reward()).sum();
    }

    public Integer nSteps() {
        return experiences.size();
    }
}
