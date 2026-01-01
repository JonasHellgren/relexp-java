package chapters.ch4.implem.treasure.factory;

import chapters.ch4.domain.trainer.param.TrainerGridParameters;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

@UtilityClass
public class FactoryTrainerParametersTreasure {

    public static TrainerGridParameters produceManyEpisodes() {
        return TrainerGridParameters.newDefault()
                .withEnvironmentName(EnvironmentTreasure.NAME)
                .withNEpisodes(100_000);
    }


    public static TrainerGridParameters produceManyEpisodesLittleExploration() {
        return produceManyEpisodes()
                .withEnvironmentName(EnvironmentTreasure.NAME)
                .withProbRandomActionStartAndEnd(Pair.create(0.1, 0.01));
    }

    public static TrainerGridParameters produceManyEpisodesMuchExploration() {
        return produceManyEpisodes()
                .withEnvironmentName(EnvironmentTreasure.NAME)
                .withProbRandomActionStartAndEnd(Pair.create(0.99, 0.01));
    }


}
