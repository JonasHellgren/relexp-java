package chapters.ch8.domain.trainer.core;

import chapters.ch8.domain.agent.core.AgentParking;
import chapters.ch8.domain.environment.core.EnvironmentParking;
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
        CpuTimer timer
) {


    public double probRandom(int step) {
        return decaying(trainerParameters.probRandomActionStartEnd(), step);
    }

    public double learningRate(int step) {
        return decaying(trainerParameters.learningRateActionValueStartEnd(),step);
    }

    public double lrRewardAverage(int step) {
        return decaying(trainerParameters.learningRateRewardAverageStartEnd(),step);
    }

    private double decaying(Pair<Double, Double> trainerParameters, int step) {
        var pr = LogarithmicDecay.of(trainerParameters, environment.getParameters().maxSteps());
        return pr.calcOut(step);
    }
}
