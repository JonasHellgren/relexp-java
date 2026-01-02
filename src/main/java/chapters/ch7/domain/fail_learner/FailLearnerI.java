package chapters.ch7.domain.fail_learner;

import chapters.ch7.domain.safety_layer.SafetyLayer;
import chapters.ch7.domain.trainer.ExperienceGridCorrectedAction;

import java.util.List;

/**
 * This interface defines the contract for fail learners, which are responsible for updating safety
 * layers based on experiences.
 */
public interface FailLearnerI {
    void updateLayer(SafetyLayer layer, List<ExperienceGridCorrectedAction> experiences);
}
