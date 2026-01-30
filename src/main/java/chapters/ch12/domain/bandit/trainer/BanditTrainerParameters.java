package chapters.ch12.domain.bandit.trainer;

import lombok.Builder;

@Builder
public record BanditTrainerParameters(
        int nEpochs,
        double learningRate,
        int batchSize,
        int nEpochsBetweenLogging
) {
}
