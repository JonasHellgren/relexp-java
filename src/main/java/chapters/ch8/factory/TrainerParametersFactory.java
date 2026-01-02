package chapters.ch8.factory;

import chapters.ch8.domain.trainer.param.TrainerParametersParking;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

/**
 * A utility class for creating trainer parameters.
 * This class provides a way to create trainer parameters for the parking domain.
 */

@UtilityClass
public class TrainerParametersFactory {

    public static final double PROB_RAND_ACTION = 0.3;
    public static final double LR_AV = 0.01;
    public static final double LR_AVGREWARD_START = 0.01;
    public static final double LR_AVGREWARD_END = 0.001;

    public static TrainerParametersParking forRunning() {
        return TrainerParametersParking.builder()
                .learningRateActionValueStartEnd(Pair.create(LR_AV, LR_AV))
                .learningRateRewardAverageStartEnd(Pair.create(LR_AVGREWARD_START, LR_AVGREWARD_END))
                .probRandomActionStartEnd(Pair.create(PROB_RAND_ACTION, PROB_RAND_ACTION))
                .build();
    }


}
