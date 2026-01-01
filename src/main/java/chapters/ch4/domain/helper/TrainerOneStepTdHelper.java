package chapters.ch4.domain.helper;

import chapters.ch4.domain.trainer.core.ExperienceGrid;
import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import core.foundation.gadget.cond.Counter;
import core.foundation.gadget.timer.CpuTimer;
import core.foundation.util.math.LogarithmicDecay;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import core.gridrl.StepReturnGrid;
import core.plotting.progress_plotting.ProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for one-step Temporal Difference (TD) trainers.
 * Provides methods for choosing actions, taking steps, creating experiences,
 * and updating agent memory.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TrainerOneStepTdHelper {

    private final TrainerGridDependencies dependencies;
    private final LogarithmicDecay learningRateDecay;
    private final LogarithmicDecay probRandomDecay;
    private final Counter stepCounter;
    private final CpuTimer timer;
    private final List<ExperienceGrid> experiences;

    public static TrainerOneStepTdHelper of(TrainerGridDependencies dependencies) {
        return new TrainerOneStepTdHelper(
                dependencies,
                createLearningRateDecay(dependencies),
                createProbRandomDecay(dependencies),
                Counter.ofMaxCount(dependencies.maxNofSteps()),
                CpuTimer.empty(),
                new ArrayList<>());
    }


    public static LogarithmicDecay createLearningRateDecay(TrainerGridDependencies d) {
        var learningRatePair = d.trainerParameters().learningRateStartAndEnd();
        return new LogarithmicDecay(learningRatePair.getFirst(),learningRatePair.getSecond(), d.getNofEpisodes());
    }


    private static LogarithmicDecay createProbRandomDecay(TrainerGridDependencies d) {
        var probRandomPair = d.trainerParameters().probRandomActionStartAndEnd();
        return new LogarithmicDecay(probRandomPair.getFirst(),probRandomPair.getSecond(), d.getNofEpisodes());
    }

    public  boolean notTerminalStateAndNotToManySteps(StateGrid s) {
        return !dependencies.isTerminal(s) && stepCounter.isNotExceeded();
    }

    public ActionGrid chooseAction(StateGrid s, int ei) {
        return dependencies.agent().chooseAction(s, probRandomDecay.calcOut(ei));
    }

    public StepReturnGrid takeAction(StateGrid s, ActionGrid action) {
        return dependencies.environment().step(s, action);
    }

    public ExperienceGrid createSarsExperience(StateGrid s, ActionGrid action, StepReturnGrid sr) {
        return ExperienceGrid.ofSars(s, action, sr);
    }

    public ExperienceGrid createSarsaExperience(StateGrid s, ActionGrid action, StepReturnGrid sr,ActionGrid actionNext) {
        return ExperienceGrid.ofSarsa(s, action, sr,actionNext);
    }


    public void updateAgentMemoryFromExperience(ExperienceGrid e, int ei) {
        dependencies.agent().fitMemory(e, learningRateDecay.calcOut(ei));
    }

    public void increaseStepCounter() {
        stepCounter.increase();
    }

    public void resetBeforeEpisode() {
        stepCounter.reset();
        experiences.clear();
    }

    public void clearTimer() {
        timer.reset();
    }

    public void saveExperienceForRecording(ExperienceGrid e) {
        experiences.add(e);
    }

    public ProgressMeasures getProgressMeasures() {
        var factory= ProgressMeasuresExtractorGrid.of(dependencies);
        return factory.produce(experiences);
    }
}
