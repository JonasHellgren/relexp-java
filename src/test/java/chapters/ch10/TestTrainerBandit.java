package chapters.ch10;


import chapters.ch10.bandit._shared.MeasuresBanditEnum;
import chapters.ch10.bandit.domain.agent.AgentBandit;
import chapters.ch10.bandit.domain.environment.EnvironmentBandit;
import chapters.ch10.bandit.domain.trainer.TrainerBandit;
import chapters.ch10.bandit.domain.trainer.TrainerDependenciesBandit;
import chapters.ch10.bandit.factory.FactoryAgentParametersBandit;
import chapters.ch10.bandit.factory.FactoryEnvironmentParametersBandit;
import chapters.ch10.bandit.factory.FactoryTrainerParameters;
import core.foundation.util.collections.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTrainerBandit {

    public static final double HIGH_PROB = 0.7;
    public static final double LOW_PROB = 1-HIGH_PROB;
    TrainerBandit trainer;

    @BeforeEach
    void init() {
        var agent = AgentBandit.of(FactoryAgentParametersBandit.equalProbability());
        var environmentLeftBetter = EnvironmentBandit.of(
                FactoryEnvironmentParametersBandit.veryHighLeftProbability());
        var trainerPar = FactoryTrainerParameters.forTest();
        var dependencies = TrainerDependenciesBandit.of(agent, environmentLeftBetter, trainerPar);
        trainer = TrainerBandit.of(dependencies);
    }

    @Test
    void whenTrained_thenProbabilitiesOk() {
        trainer.train();
        var recorder = trainer.getRecorder();
        var agent = trainer.getDependencies().agent();
        var trajG = recorder.trajectory(MeasuresBanditEnum.SUMREWARDS);
        var probs = agent.actionProbabilities();
        double avgG = ListUtil.findAverage(trajG).orElseThrow();

        Assertions.assertTrue(probs[0] > HIGH_PROB && probs[1] < LOW_PROB);
        Assertions.assertTrue(avgG > 0.5);
    }

}
