package chapters.ch12.inv_pendulum.domain.trainer.core;

import chapters.ch12.inv_pendulum.domain.agent.core.AgentPendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.StatePendulum;
import chapters.ch12.inv_pendulum.plotting.MeasuresPendulumTraining;
import chapters.ch12.inv_pendulum.plotting.RecorderTrainerPendulum;
import core.foundation.gadget.timer.CpuTimer;
import core.foundation.util.cond.Conditionals;
import core.foundation.gadget.math.Accumulator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

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
        var timer = CpuTimer.empty();
        log.info("starting training");
        var agent = dependencies.agent();
        var environment = dependencies.environment();
        var replayBuffer = ReplayBuffer.of(dependencies);
        var targetCalculator = TargetCalculator.of(dependencies);
        for (int ei = 0; ei < dependencies.getNofEpisodes(); ei++) {
            var s = dependencies.startStateSupplier().getStartState();
            boolean termState = false;
            double probRandom = dependencies.probRandom(ei);
            var accum = Accumulator.empty();
            double lr = dependencies.miniBatchDependantLearningRate(ei);
            while (!termState) {
                var a = agent.chooseAction(s, probRandom);
                var sr = environment.step(s, a);
                var mb = replayBuffer.sampleMiniBatch();
                fitAgentMemory(mb, targetCalculator, agent, lr);
                replayBuffer.add(ExperiencePendulum.of(s, a, sr));
                replayBuffer.maybeDeleteOldExperience();
                s = sr.stateNew();
                accum.add(sr.reward());
                termState = sr.isTerminal();
            }
            addRecord(ei, accum.value(), s);
            maybeLogSuccess(s, ei);
            maybeCopyToTargetNetwork(ei);
        }
        log.info("Training finished in (s): " + timer.timeInSecondsAsString());
    }

    private void fitAgentMemory(MiniBatch mb, TargetCalculator targetCalculator, AgentPendulum agent, double lr) {
        Conditionals.executeIfTrue(mb.size() >= dependencies.trainerParameters().sizeMiniBatch(),
                () -> agent.fit(mb.getStateList(), targetCalculator.calculateTargets(mb),lr));
    }

    private void maybeCopyToTargetNetwork(int i) {
        Conditionals.executeIfTrue(i % dependencies.episodesBetweenTargetUpdates() == 0,
                () -> dependencies.agent().copyParamsToTargetNet());
    }

    private void maybeLogSuccess(StatePendulum s, int i) {
        if (s.nSteps() > dependencies.environment().getParameters().maxSteps() * 0.75) {
            log.info("Episode " + i + " finished in " + s.nSteps() + " steps");
        }
    }

    private void addRecord(double ei, double sumRewards, StatePendulum s) {
        var agent = dependencies.agent();
        var state0 = StatePendulum.ofStart(0, 0);
        var avList = agent.read(state0);
        var measures = MeasuresPendulumTraining.builder()
                .episode(ei)
                .sumRewards(sumRewards)
                .time(s.nSteps()*dependencies.environment().getParameters().dt()).nSteps(s.nSteps())
                .loss(agent.loss())
                .q0ccw(avList.get(0).actionValue())
                .q0n(avList.get(1).actionValue())
                .q0cw(avList.get(2).actionValue())
                .build();
        recorder.add(measures);
    }

}
