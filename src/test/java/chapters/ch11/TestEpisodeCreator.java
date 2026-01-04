package chapters.ch11;


import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierRandomHeightZeroSpeed;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.param.TrainerParameters;
import chapters.ch11.helper.EpisodeCreator;
import chapters.ch11.helper.EpisodeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestEpisodeCreator {

    EpisodeCreator creator;

    @BeforeEach
    void init() {

        var ep = LunarParameters.defaultProps();
        var p = AgentParameters.newDefault(ep);
        var trainerDependencies = TrainerDependencies.builder()
                .agent(AgentLunar.zeroWeights(p, ep))
                .environment(EnvironmentLunar.createDefault())
                .trainerParameters(TrainerParameters.newDefault())
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
