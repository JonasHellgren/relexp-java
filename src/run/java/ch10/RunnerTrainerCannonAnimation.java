package ch10;

import chapters.ch10.animation.cannon.AnimationCannon;
import chapters.ch10.cannon.domain.agent.AgentCannon;
import chapters.ch10.cannon.domain.envrionment.EnvironmentCannon;
import chapters.ch10.cannon.domain.trainer.TrainerCannon;
import chapters.ch10.cannon.domain.trainer.TrainerDependenciesCannon;
import chapters.ch10.factory.FactoryAgentParametersCannon;
import chapters.ch10.factory.FactoryEnvironmentParametersCannon;
import chapters.ch10.factory.FactoryTrainerParametersCannon;
import chapters.ch10.plotting.ErrorBandPlotterCannon;
import chapters.ch10.plotting.MeasuresCannonEnum;
import core.foundation.config.ConfigFactory;
import lombok.SneakyThrows;
import java.util.List;

public class RunnerTrainerCannonAnimation {

    static final int N_WINDOWS_FILTERING = 10;


    @SneakyThrows
    public static void main(String[] args) {
        var trainer = getTrainer();
        var cfg= ConfigFactory.getAnimationConfig();
        trainer.train(AnimationCannon.create(cfg));
    }

    static TrainerCannon getTrainer() {
        var parEnv = FactoryEnvironmentParametersCannon.createDefault();
        var environment = EnvironmentCannon.of(parEnv);
        var parAgent =  FactoryAgentParametersCannon.animation();
        var agent = AgentCannon.of(parAgent);
        var parTrainer = FactoryTrainerParametersCannon.animation();
        var dependencies = TrainerDependenciesCannon.of(environment, agent, parTrainer);
        return TrainerCannon.of(dependencies);
    }


}
