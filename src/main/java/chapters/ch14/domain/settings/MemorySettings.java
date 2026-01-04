package chapters.ch14.domain.settings;

import chapters.ch14.environments.pong.PongSettings;
import lombok.Builder;
import lombok.With;

/**
 * This class represents the settings for the RBF memory.
 * It includes parameters such as learning rate, number of kernels per dimension,
 * relative sigma, batch size, and number of epochs.
 */
@Builder
@With
public record MemorySettings(
        double learningRate,
        int nKernelsPerDimension,
        double relSigma,
        int batchSize,
        int nEpochs
)

{

    public   double getSigmaTimeHit(PongSettings es) {
        return relSigma * (es.timeMaxHitBottom() - es.timeMinHitBottom()) / (nKernelsPerDimension-1);
    }

    public double getSigmaDeltaX(PongSettings es) {
        return relSigma * (es.yMax() - es.yMin()) / (nKernelsPerDimension-1);
    }



}
