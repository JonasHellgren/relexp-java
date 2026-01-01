package chapters.ch4.implem.cliff_walk.factory;

import chapters.ch4.domain.trainer.param.TrainerGridParameters;
import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

/**
 * Factory class for creating TrainerGridParameters instances for the Cliff Walk environment.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TrainerParametersFactoryCliff {

    public static TrainerGridParameters produceDefault() {
        return TrainerGridParameters.newDefault().withEnvironmentName(EnvironmentCliff.NAME);
    }

    public static TrainerGridParameters produceFewEpisodes() {
        return produceDefault().withNEpisodes(1000);
    }

    public static TrainerGridParameters produceManyEpisodes() {
        return produceDefault().withNEpisodes(5_000);
    }


    public static TrainerGridParameters produceHighLearningRateAndExploration() {
        return produceFewEpisodes()
                .withLearningRateStartAndEnd(Pair.create(0.9, 0.1))  //0.9
                .withProbRandomActionStartAndEnd(Pair.create(0.9, 0.01)); //0.9
    }


}
