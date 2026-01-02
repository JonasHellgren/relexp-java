package chapters.ch7.domain.fail_learner;

import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch7.domain.safety_layer.SafetyLayer;
import chapters.ch7.domain.trainer.ExperienceGridCorrectedAction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.util.List;

/**
 * This class represents an active fail learner, which updates the safety layer based on experiences.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class FailLearnerActive implements FailLearnerI {

    public static FailLearnerActive create() {
        return new FailLearnerActive();
    }

    @Override
    public void updateLayer(SafetyLayer layer, List<ExperienceGridCorrectedAction> experiences) {
        for (ExperienceGridCorrectedAction e : experiences) {
            if (e.experienceGrid().isTransitionToFail()) {
                log.info("Added fail." +
                        "State: " + e.experienceGrid().state() +
                        ", action: " + e.actionCorrected());
                var sa= StateActionGrid.of(e.experienceGrid().state(), e.actionCorrected());
                layer.add(sa);
            }
        }
    }
}
