package chapters.ch10;


import chapters.ch10.cannon.domain.agent.AgentCannon;
import chapters.ch10.cannon.domain.envrionment.EnvironmentCannon;
import chapters.ch10.cannon.domain.trainer.TrainerCannon;
import chapters.ch10.cannon.domain.trainer.TrainerDependenciesCannon;
import chapters.ch10.factory.FactoryAgentParametersCannon;
import chapters.ch10.factory.FactoryEnvironmentParametersCannon;
import chapters.ch10.factory.FactoryTrainerParametersCannon;
import core.foundation.util.unit_converter.UnitConverterUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTrainerCannon {

    public static final double TOL_ANGLE_RAD = 1e-1;
    TrainerCannon trainer;
    TrainerDependenciesCannon dependencies;

    @BeforeEach
    void init() {
        var parEnv = FactoryEnvironmentParametersCannon.createDefault();
        var environment = EnvironmentCannon.of(parEnv);
        var parAgent = FactoryAgentParametersCannon.forRunning();
        var agent = AgentCannon.of(parAgent);
        var parTrainer = FactoryTrainerParametersCannon.forRunning();
        dependencies = TrainerDependenciesCannon.of(environment, agent, parTrainer);
        trainer = TrainerCannon.of(dependencies);
    }

    @Test
    void whenTraining_thenCorrect() {
        trainer.train();

        double expAngleRad= UnitConverterUtil.convertDegreesToRadians(32);
        double expAngleMeanRad = dependencies.agent().meanAndStd().mean();
        Assertions.assertEquals(dependencies.nEpisodes(), trainer.getRecorder().size());
        Assertions.assertEquals(expAngleRad, expAngleMeanRad, TOL_ANGLE_RAD);

    }


}
