package chapters.ch11.domain.trainer.core;

import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.environment.core.EnvironmentI;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierI;
import chapters.ch11.domain.trainer.param.TrainerParameters;
import lombok.Builder;
import lombok.With;

/**
 * Represents the dependencies required for a trainer in the lunar lander domain.
 * This includes the agent, environment, trainer parameters, and start
 * state supplier.
 */

@Builder
@With
public record TrainerDependencies(
        AgentLunar agent,
        EnvironmentI environment,
        TrainerParameters trainerParameters,
        StartStateSupplierI startStateSupplier
) {

    public StateLunar startState() {
        return startStateSupplier.getStartState();
    }

    public double getGamma() {
        return trainerParameters().gamma();
    }

    public int getNofEpisodes() {
        return trainerParameters().nEpisodes();
    }

}
