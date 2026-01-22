package chapters.ch7.domain.trainer;

import core.gridrl.ExperienceGrid;
import core.gridrl.TrainerGridDependencies;
import core.gridrl.TrainerGridI;
import chapters.ch7.domain.fail_learner.FailLearnerActive;
import chapters.ch7.domain.fail_learner.FailLearnerI;
import chapters.ch7.domain.fail_learner.FailLearnerPassive;
import chapters.ch7.domain.safety_layer.SafetyLayer;
import core.foundation.gadget.cond.Counter;
import core.foundation.gadget.timer.CpuTimer;
import core.gridrl.StateGrid;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Q-Learning algorithm with safety layer for one-step Temporal Difference (TD) trainers.
 * It uses the TrainerGridDependencies object to get the required dependencies for training.
 * The RecorderProgressMeasures object is used to record the progress of training.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Log
public class TrainerOneStepTdQLearningWithSafety implements TrainerGridI {

    private final TrainerGridDependencies dependencies;
    private final RecorderProgressMeasures recorder;
    private final SafetyLayer safetyLayer;
    private final FailLearnerI failLearner;

    public static TrainerOneStepTdQLearningWithSafety givenSafetyLayerOf(TrainerGridDependencies dependencies,
                                                                         SafetyLayer safetyLayer) {
        return createTrainer(dependencies, safetyLayer, FailLearnerPassive.create());
    }

    public static TrainerOneStepTdQLearningWithSafety activeLearnerOf(TrainerGridDependencies dependencies) {
        return createTrainer(dependencies, SafetyLayer.empty(dependencies), FailLearnerActive.create());
    }

    private static TrainerOneStepTdQLearningWithSafety createTrainer(@NonNull TrainerGridDependencies dependencies,
                                                                     @NonNull SafetyLayer safetyLayer,
                                                                     @NonNull FailLearnerI failLearner) {
        return new TrainerOneStepTdQLearningWithSafety(dependencies,
                RecorderProgressMeasures.empty(),
                safetyLayer,
                failLearner);
    }

    @Override
    public void train() {
        var d = dependencies;  //gives more readable and shorter code
        recorder.clear();
        var measureExtractor = ProgressMeasureExtractorSafe.of();
        var penalizer = AgentMemoryPenalizerCorrectedAction.of(d);
        for (int ei = 0; ei < d.getNofEpisodes(); ei++) {
            var s = d.getStartState();
            var experiences = runEpisode(s, ei);
            failLearner.updateLayer(safetyLayer, experiences);
            penalizer.penalize(experiences);
            recorder.add(measureExtractor.getProgressMeasures(experiences, safetyLayer));
        }
    }

    public  void logTrainingTime() {
        log.info("Training finished in (s): " + dependencies.timer().timeInSecondsAsString());
    }

    private List<ExperienceGridCorrectedAction> runEpisode(StateGrid s, int ei) {
        var d = dependencies;
        List<ExperienceGridCorrectedAction> experiences = new ArrayList<>();
        var counter = Counter.ofMaxCount(d.maxNofSteps());
        while (!d.isTerminal(s) && counter.isNotExceeded()) {
            var a = d.chooseAction(s, ei);
            var aC = safetyLayer.correct(s, a);
            var sr = d.environment().step(s, aC);  //learn from applied action, not agent proposed
            var e = ExperienceGridCorrectedAction.ofSars(s, a, aC, sr);
            experiences.add(e);
            updateAgentMemoryFromExperience(e, d.learningRate(ei));
            s = sr.sNext();
            counter.increase();
        }
        return experiences;
    }

    private void updateAgentMemoryFromExperience(ExperienceGridCorrectedAction e, double learningRate) {
        var exp = ExperienceGrid.ofSars(e.state(), e.actionCorrected(), e.stepReturn());
        dependencies.agent().fitMemory(exp, learningRate);
    }
}
