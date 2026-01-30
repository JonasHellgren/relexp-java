package chapters.ch12.factory;

import chapters.ch12.domain.bandit.trainer.BanditTrainerParameters;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BanditTrainerParametersFactory {

    public static BanditTrainerParameters produce() {
        return BanditTrainerParameters.builder()
                .nEpochs(2_000)
                .learningRate(0.01)
                .batchSize(1)
                .nEpochsBetweenLogging(500)
                .build();
    }


}
