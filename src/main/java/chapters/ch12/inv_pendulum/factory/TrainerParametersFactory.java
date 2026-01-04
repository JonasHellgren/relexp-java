package chapters.ch12.inv_pendulum.factory;

import chapters.ch12.inv_pendulum.domain.trainer.param.TrainerParameters;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

@UtilityClass
public class TrainerParametersFactory {

    public static TrainerParameters createForTest() {
        return TrainerParameters.builder()
                .learningRateStartEnd(Pair.create(0.1,0.1))
                .discountFactor(0.9999)
                .nEpisodes(100)
                .probRandomActionStartEnd(Pair.create(0.1, 0.01))
                .episodesBetweenTargetUpdates(20)
                .sizeMiniBatch(32).sizeMiniBatchNominal(32)
                .maxSizeReplayBuffer(50_000)
                .build();
    }

    public static TrainerParameters createForTrainerTest() {
        return TrainerParameters.builder()
                .learningRateStartEnd(Pair.create(0.01,0.01))
                .discountFactor(0.99)
                .nEpisodes(100)
                .probRandomActionStartEnd(Pair.create(0.5, 0.001)) //0.1
                .episodesBetweenTargetUpdates(20)
                .sizeMiniBatch(32).sizeMiniBatchNominal(32)
                .maxSizeReplayBuffer(50_000)
                .minMaxReward(Pair.create(-1.0, 1.0))
                .build();
    }


    public static TrainerParameters createForTrainerRunning(int nEpisodes,
                                                            Pair<Double, Double> lrPair,
                                                            int sizeMiniBatch) {
        return createForTrainerTest()
                .withNEpisodes(nEpisodes)
                .withProbRandomActionStartEnd(Pair.create(0.5, 0.01))
                .withLearningRateStartEnd(lrPair).withSizeMiniBatch(sizeMiniBatch);
    }


}
