package chapters.ch14.factory;

import chapters.ch14.domain.settings.TrainerSettings;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryTrainerSettings {


    public static TrainerSettings forTest() {
        return TrainerSettings.builder()
                .sizeMiniBatch(10)
                .nFits(1)
                .maxSizeReplayBuffer(1000)
                .maxEpisodes(1000)
                .maxStepsPerEpisode(100)
                .gamma(1.0)
                .isShowLogging(false)
                .build();
    }

    public static TrainerSettings forTestLongMemFit() {
        return TrainerSettings.builder()
                .sizeMiniBatch(50)
                .nFits(10)
                .maxSizeReplayBuffer(1000)
                .maxEpisodes(300)
                .maxStepsPerEpisode(3)
                .gamma(1.0)
                .isShowLogging(false)
                .build();
    }


    public static TrainerSettings forRunning() {
        return TrainerSettings.builder()
                .sizeMiniBatch(50)
                .nFits(10)
                .maxSizeReplayBuffer(3000)
                .maxEpisodes(500)
                .maxStepsPerEpisode(3)
                .gamma(1.0)
                .isShowLogging(false)
                .build();
    }


}
