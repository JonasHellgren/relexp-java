package chapters.ch6.domain.trainer.core;


import core.gridrl.StartStateGridSupplierI;
import chapters.ch6.domain.agent.core.AgentGridMultiStepI;
import chapters.ch6.domain.trainer.param.TrainerParametersMultiStepGrid;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.math.LogarithmicDecay;
import core.gridrl.EnvironmentGridI;
import core.gridrl.StateGrid;
import lombok.With;

/**
 * Represents the dependencies required for a trainer in the multi-step domain.
 * This includes the agent, environment, trainer parameters, and start state supplier.
 * Methods makes client code cleaner
 */

@With
public record TrainerDependenciesMultiStep(
        AgentGridMultiStepI agent,
        EnvironmentGridI environment,
        TrainerParametersMultiStepGrid trainerParameters,
        StartStateGridSupplierI startStateSupplier
)

{

    public static TrainerDependenciesMultiStep of(AgentGridMultiStepI agent,
                                                  EnvironmentGridI environment,
                                                  TrainerParametersMultiStepGrid trainerParameters,
                                                  StartStateGridSupplierI startStateSupplier) {
        return new TrainerDependenciesMultiStep(agent, environment, trainerParameters, startStateSupplier);
    }


    public double getNofEpisodes() {
        return trainerParameters.nEpisodes();
    }


    public LogarithmicDecay getDecLearningRate() {
        var lrPair = trainerParameters().learningRateStartAndEnd();
        return LogarithmicDecay.of(lrPair, getNofEpisodes());
    }


    public LogarithmicDecay getDecProbRandomAction() {
        var probPair = trainerParameters().probRandomActionStartAndEnd();
        return LogarithmicDecay.of(probPair, getNofEpisodes());
    }


    public StateGrid getStartState() {
        return startStateSupplier().getStartState();
    }


    public int backupHorizon() {
        return trainerParameters().backupHorizon();
    }


    public Counter getStepCounter() {
        return Counter.ofMaxCount(trainerParameters().nStepsMax());
    }

}
