package chapters.ch7.domain.fail_learner;

import chapters.ch7.domain.safety_layer.SafetyLayer;
import chapters.ch7.domain.trainer.ExperienceGridCorrectedAction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * A passive fail learner that does not update the safety layer.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FailLearnerPassive implements FailLearnerI {

    public static FailLearnerPassive create() {
        return new FailLearnerPassive();
    }

    @Override
    public void updateLayer(SafetyLayer layer, List<ExperienceGridCorrectedAction> experiences) {
        // passive means doing nothing
    }
}
