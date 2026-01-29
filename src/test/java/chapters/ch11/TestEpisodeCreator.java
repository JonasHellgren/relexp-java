package chapters.ch11;


import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierRandomHeightZeroSpeed;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.param.TrainerParameters;
import chapters.ch11.factory.LunarAgentParamsFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import chapters.ch11.factory.TrainerParamsFactory;
import chapters.ch11.helper.EpisodeCreator;
import chapters.ch11.helper.EpisodeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestEpisodeCreator {

    EpisodeCreator creator;

    @BeforeEach
    void init() {
        var ep = LunarEnvParamsFactory.produceDefault();
        var p = LunarAgentParamsFactory.newDefault(ep);
        var tp = TrainerParamsFactory.newDefault();

        var trainerDependencies = TrainerDependencies.builder()
                .agent(AgentLunar.zeroWeights(p,tp, ep))
                .environment(EnvironmentLunar.createDefault())
                .trainerParameters(TrainerParamsFactory.newDefault())
                .startStateSupplier(StartStateSupplierRandomHeightZeroSpeed.create(ep))
                .build();
        creator = new EpisodeCreator(trainerDependencies);
    }


    @Test
    void givenNonTrainedAgent_thenWillFail() {
        var experiences = creator.create();
        var info = EpisodeInfo.of(experiences);


        System.out.println("info.endExperience() = " + info.endExperience());

        Assertions.assertTrue(info.endExperience().isTransitionToFail());
    }
}
