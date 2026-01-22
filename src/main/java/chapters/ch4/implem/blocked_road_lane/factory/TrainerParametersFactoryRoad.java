package chapters.ch4.implem.blocked_road_lane.factory;

import core.gridrl.TrainerGridParameters;
import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

/**
 * Factory class for creating TrainerGridParameters instances for the blocked road lane environment.
 */
@UtilityClass
public class TrainerParametersFactoryRoad {

    public static TrainerGridParameters produceDefault() {
        return TrainerGridParameters.newDefault()
                .withEnvironmentName(EnvironmentRoad.NAME);
    }

    public static TrainerGridParameters produceHighLearningRateAndExploration() {
        return produceDefault()
                .withLearningRateStartAndEnd(Pair.create(0.9, 0.1))  //0.9
                .withProbRandomActionStartAndEnd(Pair.create(0.9, 0.01)); //0.9
    }


    public static TrainerGridParameters produceHighLearningLowExploration() {
        return produceHighLearningRateAndExploration()
                .withProbRandomActionStartAndEnd(Pair.create(0.1, 0.01));
    }


    public static TrainerGridParameters produceLowLearningHighExploration() {
        return produceHighLearningRateAndExploration()
                .withLearningRateStartAndEnd(Pair.create(0.1, 0.1));
    }


}
