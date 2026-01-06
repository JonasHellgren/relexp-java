package chapters.ch4;

import chapters.ch4.domain.agent.AgentQLearningGrid;
import core.gridrl.TrainerGridDependencies;
import chapters.ch4.domain.trainer.TrainerOneStepTdQLearning;
import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import chapters.ch4.implem.cliff_walk.core.InformerCliff;
import chapters.ch4.implem.cliff_walk.factory.AgentGridParametersFactoryCliff;
import chapters.ch4.implem.cliff_walk.factory.FactoryEnvironmentParametersCliff;
import chapters.ch4.implem.cliff_walk.factory.TrainerParametersFactoryCliff;
import chapters.ch4.implem.cliff_walk.start_state_suppliers.StartStateSupplierCliffLowerLeft;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import shared.ArgumentsDecoder;

public class TestTrainerQLearningCliff {

    public static final double LOW_TOL_VALUE = 2;
    public static final double HIGH_TOL_VALUE = 3;
    public static final int N_EPISODES = 1000;

    static TrainerGridDependencies dependenciesQL;
    static TrainerOneStepTdQLearning trainerQL;

    @BeforeAll
    static void setUp() {
        var envParams = FactoryEnvironmentParametersCliff.produce();
        var informer= InformerCliff.create(envParams);
        var environment = EnvironmentCliff.of(envParams);
        var agentParamsQL = AgentGridParametersFactoryCliff.produceBase();
        var trainerParams = TrainerParametersFactoryCliff.produceHighLearningRateAndExploration()
                .withNEpisodes(N_EPISODES);
        var startStateSupplier = StartStateSupplierCliffLowerLeft.create();
        dependenciesQL=TrainerGridDependencies.of(
                AgentQLearningGrid.of(agentParamsQL, informer),
                environment,
                trainerParams,
                startStateSupplier,
                informer);

        trainerQL = TrainerOneStepTdQLearning.of(dependenciesQL);
        trainerQL.train();
    }

    @Test
    void trainerQL_whenTrained_thenExecutedEpisodesAndTestedActions() {
        int nEpisodes = dependenciesQL.getNofEpisodes();
        var recorder = trainerQL.getRecorder();
        int nTestedStateActions = dependenciesQL.agent().getNumberOfItemsInMemory();
        Assertions.assertEquals(recorder.getMeasuresList().size(), nEpisodes);
        Assertions.assertTrue(nTestedStateActions > 0);

    }

    @ParameterizedTest
    @CsvSource({
            "0,0,E, -101.0",   //s,a -> value
            "10,1,S, 9.0",
            "9,1,S, -101.0",
            "2,1,S, -101.0",
    })
    void trainerQL_whenMovingToTerminalState_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependenciesQL.agent();
        Assertions.assertEquals(
                decoder.value(), agent.read(decoder.state(), decoder.action()), LOW_TOL_VALUE);
    }

    @ParameterizedTest
    @CsvSource({
            "0,0,N, -2",   //s,a -> value
            "9,1,E, 8.0",
            "5,2,E, 3.0",  //10-7
            "0,2,E, -2",   //10-12
    })
    void trainerQL_whenMovingToNonTerminalState_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependenciesQL.agent();
        Assertions.assertEquals(
                decoder.value(), agent.read(decoder.state(), decoder.action()), HIGH_TOL_VALUE);
    }

}
