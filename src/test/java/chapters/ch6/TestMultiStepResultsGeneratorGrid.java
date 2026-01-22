package chapters.ch6;

import chapters.ch6.domain.trainer_dep.episode_generator.EpisodeGeneratorGrid;
import chapters.ch6.domain.trainer_dep.result_generator.MultiStepResultGrid;
import chapters.ch6.domain.trainer_dep.result_generator.MultiStepResultsGeneratorGrid;
import chapters.ch6.implem.factory.TrainerDependenciesFactorySplitting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestMultiStepResultsGeneratorGrid {

    public static final double LEARNING_RATE_START = 0.9;
    public static final int N_EPISODES = 10_000;
    EpisodeGeneratorGrid episodeGenerator1, episodeGenerator5;
    MultiStepResultsGeneratorGrid msrGenerator1, msrGenerator5;

    @BeforeEach
    void init() {
        int nStepsHorizon = 1;
        var dependencies = TrainerDependenciesFactorySplitting.givenOptimalPolicySplitting(nStepsHorizon, N_EPISODES, LEARNING_RATE_START);
        episodeGenerator1 = EpisodeGeneratorGrid.of(dependencies);
        msrGenerator1 = MultiStepResultsGeneratorGrid.of(dependencies);
        nStepsHorizon = 5;
        dependencies = TrainerDependenciesFactorySplitting.givenOptimalPolicySplitting(nStepsHorizon, N_EPISODES, LEARNING_RATE_START);
        episodeGenerator5 = EpisodeGeneratorGrid.of(dependencies);
        msrGenerator5 = MultiStepResultsGeneratorGrid.of(dependencies);

    }

    @Test
    void givenHorizon1_thenCorrect() {
        var expList = episodeGenerator1.generate(1.0);
        var msr = msrGenerator1.generate(expList);
        System.out.println("msr = " + msr);

        Assertions.assertEquals(expList.size(), msr.size());
        MultiStepResultGrid result1FromEnd = msr.resultAtStep(msr.size() - 2);
        MultiStepResultGrid resultEnd = msr.resultAtStep(msr.size() - 1);
        Assertions.assertTrue(result1FromEnd.stateFuture().isPresent());
        Assertions.assertTrue(resultEnd.stateFuture().isEmpty());
    }

    @Test
    void givenHorizon5_thenCorrect() {
        var expList = episodeGenerator5.generate(1.0);
        var msr = msrGenerator5.generate(expList);
        System.out.println("msr = " + msr);

        Assertions.assertEquals(expList.size(), msr.size());
        MultiStepResultGrid result1FromEnd = msr.resultAtStep(msr.size() - 2);
        MultiStepResultGrid resultEnd = msr.resultAtStep(msr.size() - 1);
        Assertions.assertTrue(result1FromEnd.stateFuture().isEmpty());  //present in horizon 1 case
        Assertions.assertTrue(resultEnd.stateFuture().isEmpty());
        Assertions.assertEquals(resultEnd.sumRewards(), result1FromEnd.sumRewards());  //returnMinusBase propagates backwards
    }


}
