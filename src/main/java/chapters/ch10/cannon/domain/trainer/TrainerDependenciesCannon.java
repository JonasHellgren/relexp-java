package chapters.ch10.cannon.domain.trainer;


import chapters.ch10.cannon.domain.agent.AgentCannon;
import chapters.ch10.cannon.domain.envrionment.EnvironmentCannon;
import chapters.ch10.cannon.domain.envrionment.StepReturnCannon;

/**
 * Represents the dependencies required for a trainer in the cannon domain.
 * This includes the environment, agent, and trainer parameters.
 */
public record TrainerDependenciesCannon(
        EnvironmentCannon environment,
        AgentCannon agent,
        TrainerParametersCannon parameters
) {

    public static TrainerDependenciesCannon of(EnvironmentCannon environment, AgentCannon agent, TrainerParametersCannon parameters) {
        return new TrainerDependenciesCannon(environment, agent, parameters);
    }

    public double chooseAction() {
        return agent.chooseAction();
    }

    public StepReturnCannon step(double action) {
        return environment.step(action);
    }

    public int nEpisodes() {
        return parameters.nEpisodes();
    }

    public double learningRate() {
        return parameters.learningRateStart();
    }
}
