package chapters.ch6;

import chapters.ch4.domain.memory.StateActionGrid;
import chapters.ch6.domain.trainer.core.TrainerDependenciesMultiStep;
import chapters.ch6.domain.trainer.core.TrainerStateActionControlAfterEpisode;
import chapters.ch6.implem.factory.TrainerDependenciesFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import shared.ArgumentsDecoder;

/***
 * TODO remove this test - not using multi step for treasure
 */

public class TestTrainerStateActionControlAfterEpisodeTreasure {

    public static final int N_EPISODES = 100_000;
    public static final double TOL = 0.3;
    public static final double LEARNING_RATE_START = 0.1;
    public static final double TOL_VALUE = 1.0;
    public static final int N_STEPS_HORIZON = 1;

    TrainerDependenciesMultiStep dependencies;
    TrainerStateActionControlAfterEpisode trainer;

    @BeforeEach
    @Disabled
    void init() {
        trainer =defineTrainer(N_STEPS_HORIZON);
        trainer.train();
        var memory=dependencies.agent().getMemory();
        System.out.println("memory = " + memory);
    }

    private TrainerStateActionControlAfterEpisode defineTrainer(int nStepsHorizon) {
        dependencies= TrainerDependenciesFactory.treasure(nStepsHorizon, N_EPISODES, LEARNING_RATE_START);
        return TrainerStateActionControlAfterEpisode.of(dependencies);
    }
/*
    @Test
    void whenPredicted_thenCorrect() {

        var returns= trainer.getReturnList().stream().filter(r -> r>0).toList();
        Assertions.assertTrue(MyListUtils.findMax(returns).orElseThrow()>0);
    }*/


    @ParameterizedTest
    @CsvSource({
            "3,1,S, 9.0",  //state, action  ->  value
            "3,0,E, 10.0",
            "1,1,N, -100.0",
            "1,1,S, -100.0",
    })
    @Disabled
    void trainerQL_givenFailState_whenTrained_thenCorrect(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependencies.agent();
        Assertions.assertEquals(decoder.value(),
                agent.read(StateActionGrid.of(decoder.state(), decoder.action())), TOL_VALUE);
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,   E, 0.0",  //state -> best action, dummy value
            "1,1,   E, 0.0"
    })
    @Disabled

    void trainerQL_givenState_whenTrained_thenCorrectBestAction(ArgumentsAccessor arguments) {
        var decoder = ArgumentsDecoder.of(arguments);
        var agent = dependencies.agent();
        Assertions.assertEquals(decoder.action(), agent.chooseActionNoExploration(decoder.state()));
    }


}
