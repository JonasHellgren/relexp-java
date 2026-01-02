package chapters.ch10.cannon.factory;

import chapters.ch10.cannon.domain.trainer.TrainerParametersCannon;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryTrainerParametersCannon {

    /**
     * A utility class for creating TrainerParametersCannon instances with predefined settings.
     */
    public static TrainerParametersCannon forTest() {
        return TrainerParametersCannon.builder()
                .denomMinGradLog(1e-5)
                .nEpisodes(1000)
                .learningRateStart(0.01)
                .learningRateEnd(0.01)
                .build();
    }

    public static TrainerParametersCannon forRunning() {
        return TrainerParametersCannon.builder()
                .denomMinGradLog(1e-3)
                .nEpisodes(10_000)
                .learningRateStart(1e-3)
                .learningRateEnd(1e-4)
                .build();
    }
}
