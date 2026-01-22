package chapters.ch6;

import chapters.ch6.domain.trainer_dep.episode_generator.EpisodeGeneratorGrid;
import chapters.ch6.domain.trainer_dep.episode_generator.EpisodeInfo;
import chapters.ch6.implem.factory.TrainerDependenciesFactorySplitting;
import core.foundation.util.math.MathUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestEpisodeGenerator {

    public static final double TOL = 0.01;
    EpisodeGeneratorGrid episodeGenerator;

    @BeforeEach
    void init() {
        var dependencies= TrainerDependenciesFactorySplitting.givenOptimalPolicySplitting(1, 10_000, 0.1);
        episodeGenerator = EpisodeGeneratorGrid.of(dependencies);
    }

    @Test
    void whenGenerating_thenCorrect() {
        var expList = episodeGenerator.generate(1.0);
        var info = EpisodeInfo.of(expList);
        double sumRewards = info.sumRewards();
        Assertions.assertTrue(info.nSteps() > 0);
        Assertions.assertTrue(info.endExperience().isTransitionToTerminal());
        Assertions.assertTrue(MathUtil.compareDoubleScalars(0, sumRewards, TOL) ||
                MathUtil.compareDoubleScalars(1, sumRewards, TOL));
    }


}
