package chapters.ch8.domain.trainer.core;

import chapters.ch8.domain.agent.core.AgentParking;
import chapters.ch8.domain.agent.core.ExperienceParking;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.EnvironmentParking;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.core.StepReturnParking;
import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch8.domain.trainer.param.TrainerParametersParking;
import core.foundation.gadget.math.LogarithmicDecay;
import core.foundation.gadget.timer.CpuTimer;
import lombok.Builder;
import org.apache.commons.math3.util.Pair;

/**
 * Represents the dependencies required for a trainer in the non-episodic domain.
 * This includes the agent, environment, trainer parameters, and start state supplier.
 */
@Builder
public record TrainerDependenciesParking(
        AgentParking agent,
        EnvironmentParking environment,
        TrainerParametersParking trainerParameters,
        StartStateSupplierI startStateSupplier,
        CpuTimer timer,
        LogarithmicDecay probRandomDecay,
        LogarithmicDecay learningRateRewardDecay,
        LogarithmicDecay learningRateDecay
) {


    public double probRandom(int step) {
        return probRandomDecay.calcOut(step);
    }

    public double learningRateAvgReward(int step) {
        return learningRateRewardDecay.calcOut(step);
    }


    public double learningRate(int step) {
        return learningRateDecay.calcOut(step);
    }

    public ActionParking chooseAction(AgentParking agent, StateParking stateNew, double probRandom) {
        return agent.chooseAction(stateNew, probRandom);
    }

    public StepReturnParking step(StateParking s, ActionParking a) {
        return environment.step(s, a);
    }


    public double updateAgentMemory(AgentParking agent, ExperienceParking exp, double lr) {
        return agent.fitMemory(exp, lr);
    }


}
