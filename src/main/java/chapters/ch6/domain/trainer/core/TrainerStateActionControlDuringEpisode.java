package chapters.ch6.domain.trainer.core;


import core.gridrl.ExperienceGrid;
import chapters.ch6.domain.trainer.mutlisteps_during_epis.MultiStepMemoryUpdater;
import chapters.ch6.plotting.ProgressMeasuresExtractorDuring;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.cond.Conditionals;
import core.plotting_rl.progress_plotting.RecorderProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.List;

import static java.lang.Math.max;

/***
 * Trainer class that implements the multi-step Sarsa algorithm with state-action control during an step.
 * This class is responsible for training an agent using the algorithm with state-action control.

 //pseudo code:
 * while criterion is false
 * experiences ← []
 * s ← start state
 * a ← choose action for s
 * t ← 0
 *  do
 *  s’ ← step(s,a)
 *  a’ ← choose action for s’
 *  experiences ← add(s,a,r,s’,a’)
 *  tau ← t-n+1
 *  if (tau≥0)
 *    updateQ(tau, experiences)
 *  s ← s’
 *  a ← a’
 *  t ← t+1
 *  while criterion is false
 * T ← length(experiences)
 * for tau=max(0,T-n) to T-1
 *   updateQ(tau, experiences)
 * endFor
 */

@Log
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TrainerStateActionControlDuringEpisode implements TrainerI {

    private final TrainerDependenciesMultiStep dependencies;
    private final RecorderProgressMeasures recorder;

    public static TrainerStateActionControlDuringEpisode of(TrainerDependenciesMultiStep dependencies) {
        return new TrainerStateActionControlDuringEpisode(
                dependencies,
                RecorderProgressMeasures.empty());
    }

    /**
     * Trains the agent using the multi-step Sarsa algorithm with state-action control.
     */
    @Override
    public void train() {
        var memoryUpdater = MultiStepMemoryUpdater.of(dependencies);
        var expListCreator = ExperienceListCreator.of(dependencies);
        var measureExtractor = ProgressMeasuresExtractorDuring.of(dependencies);
        recorder.clear();
        for (int i = 0; i < dependencies.getNofEpisodes(); i++) {
            var experiences = expListCreator.createExperiences(i);
            maybeLog(dependencies.getStepCounter());
            updateAgentMemory(experiences, memoryUpdater, i);
            recorder.add(measureExtractor.getProgressMeasures(experiences));
        }
    }

    private static void maybeLog(Counter stepCounter) {
        Conditionals.executeIfTrue(stepCounter.isExceeded(), () -> log.fine("nof steps exceeded"));
    }

    public void updateAgentMemory(List<ExperienceGrid> experiences, MultiStepMemoryUpdater memoryUpdater, int i) {
        double learningRate = dependencies.calcLearningRatet(i);
        int nExperiences = experiences.size();  //T in pseudo code
        int backupHorizon = dependencies.backupHorizon();  //n in pseudo code
        int stepWhereUpdatingStarts = max(0, nExperiences - backupHorizon);
        for (int tau = stepWhereUpdatingStarts; tau <= nExperiences - 1; tau++) {
            memoryUpdater.updateAgentMemory(tau, experiences, learningRate);
        }
    }


}
