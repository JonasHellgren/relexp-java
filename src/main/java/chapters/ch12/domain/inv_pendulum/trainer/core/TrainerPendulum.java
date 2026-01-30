package chapters.ch12.domain.inv_pendulum.trainer.core;

import chapters.ch12.domain.inv_pendulum.agent.memory.ActionAndItsValue;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.plotting_invpend.RecorderTrainerPendulum;
import core.foundation.gadget.math.Accumulator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.List;

/**
 * This class represents a trainer for the inverted pendulum domain.
 * It is responsible for training an agent to balance the pendulum.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
@Getter
public class TrainerPendulum {

    private final TrainerDependencies dependencies;
    private final RecorderTrainerPendulum recorder;

    public static TrainerPendulum of(TrainerDependencies dependencies) {
        return new TrainerPendulum(dependencies,
                RecorderTrainerPendulum.empty());
    }

    public void train() {
        var d=dependencies;
        var replayBuffer = ReplayBuffer.of(d);
        var targetCalculator = TargetCalculator.of(d);
        for (int ei = 0; ei < d.getNofEpisodes(); ei++) {
            var s = d.startStateSupplier().getStartState();
            boolean termState = false;
            double probRandom = d.probRandom(ei);
            var rewardAccum = Accumulator.empty();
            double lr = d.miniBatchDependantLearningRate(ei);
            while (!termState) {
                var a = d.chooseAction(s, probRandom);
                var sr = d.step(s, a);
                var mb = replayBuffer.sampleMiniBatch();
                var targets = targetCalculator.calculateTargets(mb);
                d.fitAgentMemory(mb, lr, targets);
                replayBuffer.add(ExperiencePendulum.of(s, a, sr));
                replayBuffer.maybeDeleteOldExperience();
                s = sr.stateNew();
                rewardAccum.add(sr.reward());
                termState = sr.isTerminal();
            }
            recorder.addRecord(ei, rewardAccum.value(), s,d);
            maybeLogSuccess(s, ei);
            d.maybeCopyToTargetNetwork(ei);
        }
    }


    public void logTime() {
        log.info("Time: " + dependencies.timer().timeInSecondsAsString());
    }

    private void maybeLogSuccess(StatePendulum s, int i) {
        if (s.nSteps() > dependencies.nStepsForLoggingSuccess()) {
            log.info("Episode " + i + " finished in " + s.nSteps() + " steps");
        }
    }
}
