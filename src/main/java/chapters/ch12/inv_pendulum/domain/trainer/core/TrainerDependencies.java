package chapters.ch12.inv_pendulum.domain.trainer.core;

import chapters.ch12.inv_pendulum.domain.agent.core.AgentPendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.EnvironmentPendulum;
import chapters.ch12.inv_pendulum.domain.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch12.inv_pendulum.domain.trainer.param.TrainerParameters;
import com.google.common.base.Preconditions;
import core.foundation.util.math.LogarithmicDecay;

/**
 * Represents the dependencies required for a trainer in the inverted pendulum domain.
 * This includes the agent, environment, trainer parameters, and start state supplier.
 */
public record TrainerDependencies(
        AgentPendulum agent,
        EnvironmentPendulum environment,
        TrainerParameters trainerParameters,
        StartStateSupplierI startStateSupplier
) {

    public static TrainerDependencies of(AgentPendulum agent,
                                        EnvironmentPendulum environment,
                                        TrainerParameters trainerParameters,
                                        StartStateSupplierI startStateSupplier) {
        return new TrainerDependencies(agent, environment, trainerParameters, startStateSupplier);
    }


    public int getNofEpisodes() {
        return trainerParameters().nEpisodes();
    }

    public double probRandom(int ei) {
        var pr = LogarithmicDecay.of(trainerParameters.probRandomActionStartEnd(),trainerParameters().nEpisodes());
        return pr.calcOut(ei);
    }

    public int episodesBetweenTargetUpdates() {
        return trainerParameters().episodesBetweenTargetUpdates();
    }

    public double miniBatchDependantLearningRate(int ei) {
        Preconditions.checkArgument(trainerParameters.sizeMiniBatch() > 0,"sizeMiniBatchNominal must be > 0");
        Preconditions.checkArgument(trainerParameters().nEpisodes() > 0,"nEpisodes must be > 0");
        var lr = LogarithmicDecay.of(trainerParameters.learningRateStartEnd(),trainerParameters().nEpisodes());
        return lr.calcOut(ei) *trainerParameters.sizeMiniBatch()/trainerParameters.sizeMiniBatchNominal();
    }
    
}
