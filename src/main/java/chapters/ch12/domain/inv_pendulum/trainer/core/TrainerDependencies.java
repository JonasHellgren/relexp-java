package chapters.ch12.domain.inv_pendulum.trainer.core;

import chapters.ch12.domain.inv_pendulum.agent.core.AgentPendulum;
import chapters.ch12.domain.inv_pendulum.agent.memory.ActionAndItsValue;
import chapters.ch12.domain.inv_pendulum.environment.core.ActionPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.EnvironmentPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StepReturnPendulum;
import chapters.ch12.domain.inv_pendulum.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch12.domain.inv_pendulum.trainer.param.TrainerParameters;
import com.google.common.base.Preconditions;
import core.foundation.gadget.math.LogarithmicDecay;
import core.foundation.gadget.timer.CpuTimer;
import core.foundation.util.cond.ConditionalsUtil;

import java.util.List;

/**
 * Represents the dependencies required for a trainer in the inverted pendulum domain.
 * This includes the agent, environment, trainer parameters, and start state supplier.
 */
public record TrainerDependencies(
        AgentPendulum agent,
        EnvironmentPendulum environment,
        TrainerParameters trainerParameters,
        StartStateSupplierI startStateSupplier,
        CpuTimer timer,
        LogarithmicDecay randomDecay,
        LogarithmicDecay learningDecay
) {

    public static TrainerDependencies of(AgentPendulum agent,
                                        EnvironmentPendulum environment,
                                        TrainerParameters trainerParameters,
                                        StartStateSupplierI startStateSupplier) {
        var tp = trainerParameters;
        return new TrainerDependencies(agent,
                environment,
                tp,
                startStateSupplier,
                CpuTimer.empty(),
                LogarithmicDecay.of(tp.probRandomActionStartEnd(), tp.nEpisodes()),
                LogarithmicDecay.of(tp.learningRateStartEnd(),tp.nEpisodes())
                );
    }


    public int getNofEpisodes() {
        return trainerParameters().nEpisodes();
    }

    public double probRandom(int ei) {
        return randomDecay.calcOut(ei);
    }

    public int episodesBetweenTargetUpdates() {
        return trainerParameters().episodesBetweenTargetUpdates();
    }

    public double miniBatchDependantLearningRate(int ei) {
        Preconditions.checkArgument(trainerParameters.sizeMiniBatch() > 0,"sizeMiniBatchNominal must be > 0");
        Preconditions.checkArgument(trainerParameters().nEpisodes() > 0,"nEpisodes must be > 0");
        return learningDecay.calcOut(ei) *trainerParameters.sizeMiniBatch()/trainerParameters.sizeMiniBatchNominal();
    }


    StepReturnPendulum step(StatePendulum s, ActionPendulum a) {
        return environment.step(s, a);
    }


    int nStepsForLoggingSuccess() {
        return (int) (environment().getParameters().maxSteps() * 0.75);
    }

    void fitAgentMemory(MiniBatch mb, double lr, List<ActionAndItsValue> targets) {
        ConditionalsUtil.executeIfTrue(mb.size() >= trainerParameters().sizeMiniBatch(),
                () -> agent.fit(mb.getStateList(), targets,lr));
    }

    void maybeCopyToTargetNetwork(int i) {
        ConditionalsUtil.executeIfTrue(i % episodesBetweenTargetUpdates() == 0,
                () -> agent().copyParamsToTargetNet());
    }


    public ActionPendulum chooseAction(StatePendulum s, double probRandom) {
        return agent().chooseAction(s, probRandom);
    }
}
