package ch10;

import chapters.ch10.bandit.domain.agent.AgentBandit;
import chapters.ch10.bandit.domain.environment.EnvironmentBandit;
import chapters.ch10.bandit.domain.trainer.TrainerBandit;
import chapters.ch10.bandit.domain.trainer.TrainerDependenciesBandit;
import chapters.ch10.factory.FactoryAgentParametersBandit;
import chapters.ch10.factory.FactoryEnvironmentParametersBandit;
import chapters.ch10.factory.FactoryTrainerParameters;
import chapters.ch9.animation.AnimationBandit;
import core.foundation.config.ConfigFactory;
import lombok.SneakyThrows;

public class RunnerTrainerBanditAnimation {

    static final int N_WINDOWS_FILTERING = 10;

    @SneakyThrows
    public static void main(String[] args) {
        var agent = AgentBandit.of(FactoryAgentParametersBandit.equalProbability());
        var environmentLeftBetter = EnvironmentBandit.of(
                FactoryEnvironmentParametersBandit.veryHighLeftProbability());
        var trainerPar = FactoryTrainerParameters.moderateLearningRateModerateEpis();
        var dependencies = TrainerDependenciesBandit.of(agent, environmentLeftBetter, trainerPar);
        var trainer = TrainerBandit.of(dependencies);

        var cfg= ConfigFactory.getAnimationConfig();
        var animation = AnimationBandit.create(cfg);
        trainer.train(animation);
    }

}
