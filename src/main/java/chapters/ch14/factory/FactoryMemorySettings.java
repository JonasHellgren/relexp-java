package chapters.ch14.factory;

import chapters.ch14.domain.settings.MemorySettings;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryMemorySettings {

    public static MemorySettings forTest() {
        return MemorySettings.builder()
                .learningRate(0.1)
                .nKernelsPerDimension(15)
                .relSigma(0.75)
                .batchSize(10)
                .nEpochs(1)
                .build();
    }

    public static MemorySettings forRunning() {
        return forTest()
                .withLearningRate(0.5).withNKernelsPerDimension(10);
    }

}
