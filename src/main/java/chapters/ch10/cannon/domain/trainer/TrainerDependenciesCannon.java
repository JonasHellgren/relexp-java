package chapters.ch10.cannon.domain.trainer;


import chapters.ch10.cannon.domain.agent.AgentCannon;
import chapters.ch10.cannon.domain.envrionment.EnvironmentCannon;
import chapters.ch10.cannon.domain.envrionment.StepReturnCannon;
import core.foundation.gadget.math.LogarithmicDecay;
import core.foundation.gadget.math.MeanAndStd;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;

/**
 * Represents the dependencies required for a trainer in the cannon domain.
 * This includes the environment, agent, and trainer parameters.
 */
public record TrainerDependenciesCannon(
        EnvironmentCannon environment,
        AgentCannon agent,
        TrainerParametersCannon parameters,
        LogarithmicDecay learningRateDecay,
        GradLogCalculatorContinuousAction gradLogCalculator
) {

    public static TrainerDependenciesCannon of(EnvironmentCannon environment,
                                               AgentCannon agent,
                                               TrainerParametersCannon tp) {
        var rateDecay = LogarithmicDecay.of(tp.learningRateStart(),tp.learningRateEnd(),tp.nEpisodes());
        return new TrainerDependenciesCannon(environment,
                agent,
                tp,
                rateDecay,
                GradLogCalculatorContinuousAction.of(tp));
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

    public double learningRate(int episode) {
        return learningRateDecay.calcOut(episode);
    }

    public GradientMeanAndLogStd calcGradLog(double action) {
        return gradLogCalculator.gradLog(action,agent.meanAndStd());
    }

    public void updateAgentMemory(double lr, double v, GradientMeanAndLogStd gradLog) {
        agent.updateMemory(lr, v, gradLog);
    }

    public MeanAndStd meanAndStd() {
        return agent.meanAndStd();
    }
}
