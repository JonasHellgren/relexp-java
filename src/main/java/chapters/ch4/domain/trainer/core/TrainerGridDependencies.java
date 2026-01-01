package chapters.ch4.domain.trainer.core;


import chapters.ch4.domain.agent.AgentGridI;
import chapters.ch4.domain.start_state_supplier.StartStateGridSupplierI;
import chapters.ch4.domain.trainer.param.TrainerGridParameters;
import core.foundation.util.math.LogarithmicDecay;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;
import lombok.Builder;
import lombok.NonNull;
import lombok.With;

/**
 * Represents the dependencies required for a grid world trainer.
 * This includes the agent, environment, trainer parameters, and start
 * state supplier.
 */

@Builder
@With
public record TrainerGridDependencies(
        @NonNull AgentGridI agent,
        @NonNull EnvironmentGridI environment,
        @NonNull TrainerGridParameters trainerParameters,
        @NonNull StartStateGridSupplierI startStateSupplier
) {

    public static TrainerGridDependencies of(AgentGridI agent,
                                             EnvironmentGridI environment,
                                             TrainerGridParameters trainerParameters,
                                             StartStateGridSupplierI startStateSupplier) {
    return new TrainerGridDependencies(agent, environment, trainerParameters, startStateSupplier);
    }

    public StateGrid getStartState() {
        return startStateSupplier.getStartState();
    }

    public boolean isTerminal(StateGrid s) {
        return environment.getParameters().isTerminal(s);
    }

    public int getNofEpisodes() {
        return trainerParameters().nEpisodes();
    }

    public Integer maxNofSteps() {
        return trainerParameters().nStepsMax();
    }

    public boolean isCorrectEnvironmentNamesForParameters() {
        String name = environment.name();
        return agent.getAgentParameters().environmentName().equals(name) &&
                trainerParameters.environmentName().equals(name) &&
                startStateSupplier.environmentName().equals(name);
    }


    public LogarithmicDecay getProbDecay() {
        var prStartAndEnd = trainerParameters.probRandomActionStartAndEnd();
        return LogarithmicDecay.of(
                prStartAndEnd.getFirst(),
                prStartAndEnd.getSecond(),
                trainerParameters.nEpisodes());
    }

    public LogarithmicDecay getLearningRateDecay() {
        var lrStartAndEnd = trainerParameters.learningRateStartAndEnd();
        return LogarithmicDecay.of(
                lrStartAndEnd.getFirst(),
                lrStartAndEnd.getSecond(),
                trainerParameters.nEpisodes());
    }


}
