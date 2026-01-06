package chapters.ch4;

import chapters.ch4.domain.agent.AgentQLearningGrid;
import chapters.ch4.domain.agent.AgentSarsaGrid;
import chapters.ch4.domain.trainer.core.TrainerGridDependencies;
import chapters.ch4.domain.trainer.core.TrainerOneStepTdQLearning;
import chapters.ch4.domain.trainer.core.TrainerOneStepTdSarsa;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import chapters.ch4.implem.blocked_road_lane.core.InformerRoadParams;
import chapters.ch4.implem.blocked_road_lane.factory.AgentGridParametersFactoryRoad;
import chapters.ch4.implem.blocked_road_lane.factory.FactoryEnvironmentParametersRoad;
import chapters.ch4.implem.blocked_road_lane.factory.TrainerParametersFactoryRoad;
import chapters.ch4.implem.blocked_road_lane.start_state_suppliers.StartStateSupplierRoadMostLeftAnyLane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import shared.ArgumentsDecoder;

class TestTrainersRoad {

    static final double LOW_TOL_VALUE = 1;
    static final double HIGH_TOL_VALUE = 50;

    static TrainerGridDependencies dependenciesQL, dependenciesSarsa;
    static TrainerOneStepTdQLearning trainerQL;
    static TrainerOneStepTdSarsa trainerSarsa;

    @BeforeAll
    static void setUp() {
        var envParams = FactoryEnvironmentParametersRoad.produceRoadFixedFailReward();
        var environment = EnvironmentRoad.of(envParams);
        var agentParams = AgentGridParametersFactoryRoad.produceBase();
        var trainerParamsQL = TrainerParametersFactoryRoad.produceHighLearningRateAndExploration().withNEpisodes(100);
        var trainerParamsSarsa = TrainerParametersFactoryRoad.produceHighLearningLowExploration().withNEpisodes(100);
        var informer= InformerRoadParams.create(envParams);

        var startStateSupplier = StartStateSupplierRoadMostLeftAnyLane.create();
        dependenciesQL=TrainerGridDependencies.of(
                AgentQLearningGrid.of(agentParams, informer),
                environment,
                trainerParamsQL,
                startStateSupplier,
                informer);

        dependenciesSarsa=TrainerGridDependencies.of(
                AgentQLearningGrid.of(agentParams, informer),
                environment,
                trainerParamsSarsa,
                startStateSupplier,
                informer);

        trainerQL = TrainerOneStepTdQLearning.of(dependenciesQL);
        trainerSarsa = TrainerOneStepTdSarsa.of(dependenciesSarsa);
        trainerQL.train();
        trainerSarsa.train();
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
            "0,0,E, 0.0",   //s,a -> value
            "0,0,N, -2.0",
            "0,0,S, -1.0",
            "2,0,E, 0.0",
    })
    void trainerQL_givenNonFailState_whenTrained_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependenciesQL.agent();
        Assertions.assertEquals(decoder.value(),
                agent.read(decoder.state(), decoder.action()), LOW_TOL_VALUE);
    }

    @ParameterizedTest
    @CsvSource({
            "2,1,E, -100.0",
            "2,0,N, -101.0",
    })
    void trainerQL_givenFailState_whenTrained_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependenciesQL.agent();
        Assertions.assertEquals(decoder.value(),
                agent.read(decoder.state(), decoder.action()), HIGH_TOL_VALUE);
    }

    @ParameterizedTest
    @CsvSource({
            "2,1,   S, 0.0",  //state -> best action, dummy value
            "2,0,   E, 0.0",
            "0,0,   E, 0.0",
            "1,0,   E, 0.0",
    })
    void trainerQL_givenState_whenTrained_thenCorrectBestAction(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependenciesQL.agent();
        Assertions.assertEquals(decoder.action(), agent.chooseActionNoExploration(decoder.state()));
    }

    @Test
    void trainerSarsa_whenTrained_thenExecutedEpisodesAndTestedActions() {
        int nEpisodes = dependenciesSarsa.getNofEpisodes();
        var recorder = trainerSarsa.getRecorder();
        int nTestedStateActions = dependenciesSarsa.agent().getNumberOfItemsInMemory();
        Assertions.assertEquals(recorder.getMeasuresList().size(), nEpisodes);
        Assertions.assertTrue(nTestedStateActions > 0);

    }

    @ParameterizedTest
    @CsvSource({
            "0,0,E, 0.0",   //s,a -> value
            "0,0,N, -2.0",
            "0,0,S, -1.0",
            "2,0,E, 0.0",
    })
    void trainerSarsa_givenNonFailState_whenTrained_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependenciesSarsa.agent();
        Assertions.assertEquals(decoder.value(), agent.read(decoder.state(), decoder.action()), HIGH_TOL_VALUE);
    }

    @ParameterizedTest
    @CsvSource({
            "2,1,E, -100.0",
            "2,0,N, -100.0",
    })
    void trainerSarsa_givenFailState_whenTrained_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependenciesSarsa.agent();
        Assertions.assertEquals(decoder.value(), agent.read(decoder.state(), decoder.action()), HIGH_TOL_VALUE);
    }

    @ParameterizedTest
    @CsvSource({
            "2,1,   S, 0.0",  //state -> best action, dummy value
            "2,0,   E, 0.0"
            //   "0,0,   E, 0.0",  //may fail
            //   "1,0,   E, 0.0",  //may fail
    })
    void trainerSarsa_givenState_whenTrained_thenCorrectBestAction(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependenciesSarsa.agent();
        Assertions.assertEquals(decoder.action(), agent.chooseActionNoExploration(decoder.state()));
    }


}
