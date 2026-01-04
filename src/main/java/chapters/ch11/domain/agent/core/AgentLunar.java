package chapters.ch11.domain.agent.core;

import chapters.ch11.domain.agent.memory.ActorMemoryLunar;
import chapters.ch11.domain.agent.memory.CriticMemoryLunar;
import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import com.google.common.base.Preconditions;
import core.foundation.gadget.math.MeanAndStd;
import core.foundation.gadget.normal_distribution.NormDistributionSampler;
import core.foundation.gadget.training.TrainDataOld;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
import core.nextlevelrl.gradient.NormalDistributionGradientCalculator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class represents an agent for the Lunar Lander game.
 * It uses radial basis functions for the actor and the critic.
 */

@AllArgsConstructor
@Getter
public class AgentLunar implements AgentI{

    AgentParameters agentParameters;
    ActorMemoryLunar actorMemory;
    CriticMemoryLunar criticMemory;
    NormDistributionSampler sampler;
    NormalDistributionGradientCalculator gradCalc;

    public static AgentLunar zeroWeights(AgentParameters p, LunarParameters ep) {
        var actMemory = ActorMemoryLunar.create(p, ep);
        var criticMemory = CriticMemoryLunar.zeroWeights(p, ep);
        var sampler = new NormDistributionSampler();
        var gc=new NormalDistributionGradientCalculator();
        return new AgentLunar(p,actMemory, criticMemory,sampler,gc);
    }

    /**
     * Chooses an action based on the current state.
     *
     * @param state the current state
     * @return the chosen action
     */
    @Override
    public double chooseAction(StateLunar state) {
        var meanAndStd= readActor(state);
        return sampler.sampleFromNormDistribution(meanAndStd.mean(),meanAndStd.std());
    }

    @Override
    public double chooseActionNoExploration(StateLunar state) {
        var meanAndStd= readActor(state);
        double std = 0;
        return sampler.sampleFromNormDistribution(meanAndStd.mean(), std);
    }

    @Override
    public void fitCritic(TrainDataOld data) {
        criticMemory.fit(data);
    }

    @Override
    public void fitActorUseCriticActivations(TrainDataOld dataMean, TrainDataOld dataStd) {
        validate();
        actorMemory.fitUsingActivationsOtherRbfMean(dataMean,dataStd,criticMemory.getMemory());
    }

    @Override
    public double readCritic(StateLunar state) {
        return criticMemory.read(state);
    }

    @Override
    public MeanAndStd readActor(StateLunar state) {
        return actorMemory.actorMeanAndStd(state);
    }


    /**
     * Computes the gradient of the actor's policy for the given state and action.
     *
     * @param state  the state
     * @param action the action
     * @return the gradient of the actor's policy
     */
    @Override
    public GradientMeanAndLogStd gradientMeanAndLogStd(StateLunar state, double action) {
        var meanAndStd= readActor(state);
        var meanAndLogStd=actorMemory.actorMeanAndLogStd(state);
        double gradMean = gradCalc.gradientMean(action, meanAndStd.mean(), meanAndStd.std());
        double gradLogStd = gradCalc.gradientLogStd(action, meanAndStd.mean(), meanAndLogStd.logStd());
        return GradientMeanAndLogStd.of(gradMean, gradLogStd);
    }

    private void validate() {
        Preconditions.checkArgument(actorMemory.getMemoryMean().nKernels() == criticMemory.getMemory().nKernels(),
                "nKernels must be the same");
    }


}
