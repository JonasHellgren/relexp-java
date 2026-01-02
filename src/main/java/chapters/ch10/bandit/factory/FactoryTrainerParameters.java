package chapters.ch10.bandit.factory;

import chapters.ch10.bandit.domain.trainer.TrainerParametersBandit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryTrainerParameters {


    public static TrainerParametersBandit forTest() {
        return TrainerParametersBandit.builder().learningRate(0.1).nEpisodes(100).build();
    }

    public static TrainerParametersBandit highLearningRateFewEpis() {
        return TrainerParametersBandit.builder().learningRate(0.9).nEpisodes(100).build();
    }

    public static TrainerParametersBandit lowLearningRateManyEpis() {
        return TrainerParametersBandit.builder().learningRate(0.01).nEpisodes(10_000).build();
    }

}
