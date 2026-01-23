package chapters.ch11.factory;

import chapters.ch11.domain.trainer.param.TrainerParameters;
import lombok.With;
import lombok.experimental.UtilityClass;

@UtilityClass
@With
public class TrainerParamsFactory {

    public static TrainerParameters newDefault() {
        return TrainerParameters.builder()
                .nStepsMax(100)
                .nEpisodes(5000)
                .gamma(0.99)
                .nStepsHorizon(5)
                .build();
    }

    public static TrainerParameters of(int stepHorizon, int nEpisodes) {
     return newDefault().withNEpisodes(nEpisodes).withNStepsHorizon(stepHorizon);
    }
}
