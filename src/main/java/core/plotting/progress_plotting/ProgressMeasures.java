package core.plotting.progress_plotting;

import lombok.Builder;
import lombok.With;

/**
 * Represents a set of progress measures for a single step.
 * Some measures may be redundant for some environments
 */
@With
@Builder
public record ProgressMeasures(
        Double sumRewards, // sum of rewards collected during the step
        Integer nSteps, // number of steps taken during the step
        Double tdError, // average TD error during the step
        Double tdBestAction, // average TD error of the best action during the step
        Double tdErrorClipped, // average clipped TD error during the step
        Double gradMean, // average gradient of mean during the step
        Double gradMeanClipped, // average clipped gradient mean during the step
        Double stdActor, // average standard deviation of the actor during the step
        Integer nMemory // number of items in the memory
) {


}
