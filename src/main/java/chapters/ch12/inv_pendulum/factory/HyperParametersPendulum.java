package chapters.ch12.inv_pendulum.factory;

import lombok.Builder;
import lombok.With;
import org.apache.commons.math3.util.Pair;

@Builder
@With
public record HyperParametersPendulum(
        int nHiddenLayers,
        int nHiddenUnits,
        boolean isFailPenalty,
        boolean isRandomStart,
        int nEpisodes,
        Pair<Double, Double> learningRateStartEnd,
        int sizeMiniBatch
) {
    public static final int N_EPISODES = 300;
    public static final double LEARNING_RATE0 = 0.01,LEARNING_RATE1 = 0.001;
    public static final int SIZE_MINI_BATCH = 32;  //16
    public static final int N_HIDDEN_UNITS = 32;  //16
    public static final int N_HIDDEN_LAYERS = 2;  //1

    public static final HyperParametersPendulum FAIL_PEN_START_UPRIGHT =
            HyperParametersPendulum.builder()
                    .nHiddenLayers(N_HIDDEN_LAYERS)
                    .nHiddenUnits(N_HIDDEN_UNITS)
                    .isFailPenalty(true)
                    .isRandomStart(false)
                    .nEpisodes(N_EPISODES)
                    .learningRateStartEnd(Pair.create(LEARNING_RATE0, LEARNING_RATE1))
                    .sizeMiniBatch(SIZE_MINI_BATCH)
                    .build();

    public static final HyperParametersPendulum CLOSE_TO_REF_START_UPRIGHT =
            HyperParametersPendulum.builder()
                    .nHiddenLayers(N_HIDDEN_LAYERS)
                    .nHiddenUnits(N_HIDDEN_UNITS)
                    .isFailPenalty(false)
                    .isRandomStart(false)
                    .nEpisodes(N_EPISODES)
                    .learningRateStartEnd(Pair.create(LEARNING_RATE0, LEARNING_RATE1))
                    .sizeMiniBatch(SIZE_MINI_BATCH)
                    .build();

    public static final HyperParametersPendulum FAIL_PEN_START_RANDOM =
            FAIL_PEN_START_UPRIGHT.withRandomStart(true);

    public static final HyperParametersPendulum CLOSE_TO_REF_START_RANDOM =
            CLOSE_TO_REF_START_UPRIGHT.withRandomStart(true);

}