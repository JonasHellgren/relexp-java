package chapters.ch11.factory;

import chapters.ch11.domain.trainer.param.TrainerParameters;
import lombok.With;
import lombok.experimental.UtilityClass;

@UtilityClass
@With
public class TrainerParamsFactory {

    static final double LEARNING_RATE_CRITIC = 1e-2;
    static final double LEARNING_RATE_ACTOR = 1e-2;

    public static TrainerParameters newDefault() {
        return TrainerParameters.builder()
                .nStepsMax(100)
                .nEpisodes(5000)
                .gamma(0.99)
                .nStepsHorizon(5)
                .learningRateCritic(LEARNING_RATE_CRITIC)
                .learningRateActor(LEARNING_RATE_ACTOR)
                .nFits(1)
                .build();
    }

    public static TrainerParameters of(int stepHorizon, int nEpisodes) {
     return newDefault().withNEpisodes(nEpisodes).withNStepsHorizon(stepHorizon);
    }
}
