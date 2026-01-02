package chapters.ch10.bandit.domain.trainer;

import chapters.ch10.bandit.domain.agent.AgentBandit;
import chapters.ch10.bandit.domain.environment.ActionBandit;
import chapters.ch10.bandit.domain.environment.EnvironmentBandit;
import chapters.ch10.bandit.domain.environment.StepReturnBandit;
import lombok.NonNull;

/**
 * Represents the dependencies required for a trainer in the bandit domain.
 * This includes the agent, environment, and trainer parameters.
 */
public record TrainerDependenciesBandit(
        AgentBandit agent,
        EnvironmentBandit environment,
        TrainerParametersBandit trainerParameters
) {


    public static TrainerDependenciesBandit of(@NonNull AgentBandit agent,
                                               @NonNull EnvironmentBandit environment,
                                               @NonNull TrainerParametersBandit trainerParameters) {
        return new TrainerDependenciesBandit(agent, environment, trainerParameters);
    }

    public int nEpisodes() {
        return trainerParameters.nEpisodes();
    }

    public ActionBandit chooseAction() {
        return agent.chooseAction();
    }

    public StepReturnBandit step(ActionBandit action) {
        return environment.step(action);
    }
}
