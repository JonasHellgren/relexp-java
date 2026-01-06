package chapters.ch6.domain.trainer.core;


import core.gridrl.StateActionGrid;
import core.gridrl.ExperienceGrid;
import chapters.ch6.domain.trainer.mutlisteps_during_epis.MultiStepMemoryUpdater;
import chapters.ch6.domain.trainer.mutlisteps_during_epis.ProgressMeasuresExtractorDuring;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.cond.Conditionals;
import core.foundation.util.math.LogarithmicDecay;
import core.plotting.progress_plotting.RecorderProgressMeasures;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import java.util.ArrayList;
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

    record Support(LogarithmicDecay decLearningRate, LogarithmicDecay decProbRandomAction, Counter stepCounter) {
        public static Support of(TrainerDependenciesMultiStep dependencies) {
            return new Support(
                    dependencies.getDecLearningRate(),
                    dependencies.getDecProbRandomAction(),
                    dependencies.getStepCounter());
        }

        public double learningRate(int i) {
            return decLearningRate.calcOut(i);
        }

        public double probRandomAction(int i) {
            return decProbRandomAction.calcOut(i);
        }

        public int timeCount() {
            return stepCounter.getCount();
        }

    }

    private final TrainerDependenciesMultiStep dependencies;
    private final RecorderProgressMeasures recorder;
    private final MultiStepMemoryUpdater memoryUpdater;

    public static TrainerStateActionControlDuringEpisode of(TrainerDependenciesMultiStep dependencies) {
        return new TrainerStateActionControlDuringEpisode(
                dependencies,
                RecorderProgressMeasures.empty(),
                MultiStepMemoryUpdater.of(dependencies));
    }

    /**
     * Trains the agent using the multi-step Sarsa algorithm with state-action control.
     */
    @Override
    public void train() {
        var support=Support.of(dependencies);
        var measureExtractor= ProgressMeasuresExtractorDuring.of(dependencies,memoryUpdater);
        recorder.clear();
        for (int i = 0; i < dependencies.getNofEpisodes(); i++) {
            var experiences = createExperiences(support,i);
            maybeLog(support.stepCounter);
            updateRemaining(experiences, support.learningRate(i));
            recorder.add(measureExtractor.getProgressMeasures(experiences));
        }
    }

    private static void maybeLog(Counter stepCounter) {
        Conditionals.executeIfTrue(stepCounter.isExceeded(), () -> log.fine("nof steps exceeded"));
    }

    private List<ExperienceGrid> createExperiences(Support support, int i) {
        List<ExperienceGrid> experiences = new ArrayList<>();
        var agent = dependencies.agent();
        var environment = dependencies.environment();
        double probRandom = support.probRandomAction(i);
        var startState = dependencies.getStartState();
        var sa= StateActionGrid.of(startState,agent.chooseAction(startState, probRandom));
        boolean isTerminal;
        support.stepCounter().reset();
        do {
            var sr = environment.step(sa.state(), sa.action());
            var stateAfterStep = sr.sNext();
            var actionNext = agent.chooseAction(stateAfterStep, probRandom);
            experiences.add(ExperienceGrid.ofSarsa(sa.state(), sa.action(), sr, actionNext));
            int tau = support.timeCount()- dependencies.backupHorizon();
            Conditionals.executeIfTrue(tau >= 0, () ->
                    memoryUpdater.updateAgentMemory(tau, experiences, support.learningRate(i)));
            sa=StateActionGrid.of(stateAfterStep, actionNext);
            isTerminal = sr.isTerminal();
            support.stepCounter.increase();
        } while (!isTerminal && support.stepCounter.isNotExceeded());
        return experiences;
    }

    private void updateRemaining(List<ExperienceGrid> experiences, double learningRate) {
        int nExperiences = experiences.size();  //T in pseudo code
        int backupHorizon = dependencies.backupHorizon();  //n in pseudo code
        int firstOfRemaining = max(0,nExperiences - backupHorizon);
        for (int tau = firstOfRemaining; tau <= nExperiences - 1; tau++) {
            memoryUpdater.updateAgentMemory(tau, experiences, learningRate);
        }
    }


}
