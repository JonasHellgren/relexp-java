package chapters.ch11.domain.agent.core;


import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.StateLunar;
import core.foundation.gadget.math.MeanAndStd;
import core.foundation.gadget.training.TrainDataOld;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;

public interface AgentI {
    double chooseAction(StateLunar state);
    double chooseActionNoExploration(StateLunar state);
    void fitCritic(TrainDataOld data);
    double readCritic(StateLunar state);
    AgentParameters getAgentParameters();
    MeanAndStd readActor(StateLunar state);
    GradientMeanAndLogStd gradientMeanAndLogStd(StateLunar state, double action);
    void fitActorUseCriticActivations(TrainDataOld dataMean, TrainDataOld dataStd);
}
