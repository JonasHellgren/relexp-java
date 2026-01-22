package chapters.ch7.domain.trainer;

import chapters.ch7.domain.safety_layer.SafetyLayer;
import core.plotting_rl.progress_plotting.ProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * A utility class for extracting progress measures from a list of
 * experiences and a safety layer.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressMeasureExtractorSafe {

    public static ProgressMeasureExtractorSafe of() {
        return new ProgressMeasureExtractorSafe();
    }

    public ProgressMeasures getProgressMeasures(List<ExperienceGridCorrectedAction> experiences, SafetyLayer safetyLayer) {
        var info = EpisodeInfoSafe.of(experiences);
        return ProgressMeasures.builder()
                .sumRewards(info.sumRewards())
                .nSteps(info.nSteps())
                .nMemory(safetyLayer.memorySize())
                .build();
    }

}
