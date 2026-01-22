package chapters.ch6.domain.trainer_dep.core;


import chapters.ch6.domain.trainer_dep.result_generator.MultiStepResultGrid;
import core.gridrl.StartStateGridSupplierI;
import chapters.ch6.domain.agent.AgentGridMultiStepI;
import chapters.ch6.domain.trainer_dep.param.TrainerParametersMultiStepGrid;
import core.foundation.gadget.cond.Counter;
import core.foundation.gadget.math.LogarithmicDecay;
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
        StartStateGridSupplierI startStateSupplier,
        LogarithmicDecay decLearningRate,
        LogarithmicDecay decProbRandomAction,
        Counter stepCounter)

{

    public static TrainerDependenciesMultiStep of(AgentGridMultiStepI agent,
                                                  EnvironmentGridI environment,
                                                  TrainerParametersMultiStepGrid trainerParameters,
                                                  StartStateGridSupplierI startStateSupplier) {
        var tp = trainerParameters;
        return new TrainerDependenciesMultiStep(agent,
                environment,
                tp,
                startStateSupplier,
                LogarithmicDecay.of(tp.learningRateStartAndEnd(), tp.nEpisodes()),
                LogarithmicDecay.of(tp.probRandomActionStartAndEnd(), tp.nEpisodes()),
                Counter.ofMaxCount(trainerParameters.nStepsMax()));
    }


    public double getNofEpisodes() {
        return trainerParameters.nEpisodes();
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

    public double calcLearningRatet(int i) {
        return decLearningRate.calcOut(i);
    }

    public double calcProbRandomActiont(int i) {
        return decProbRandomAction.calcOut(i);
    }

    public void fitAgent(MultiStepResultGrid msrAtStep, double learningRate) {
        agent().fit(msrAtStep, learningRate);
    }


}
