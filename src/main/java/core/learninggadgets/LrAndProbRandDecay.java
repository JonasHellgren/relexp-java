package core.learninggadgets;

import core.gridrl.TrainerGridParameters;
import core.foundation.util.math.LogarithmicDecay;
import org.apache.commons.math3.util.Pair;

public record LrAndProbRandDecay(LogarithmicDecay lrDecay, LogarithmicDecay probDecay) {

    public static LrAndProbRandDecay of(LogarithmicDecay lrDecay, LogarithmicDecay probDecay) {
        return new LrAndProbRandDecay(lrDecay, probDecay);
    }

    public static LrAndProbRandDecay of(Pair<Double, Double> lrStartEnd, Pair<Double, Double> probStartEnd, int nEpisodes) {
        return LrAndProbRandDecay.of(
        new LogarithmicDecay(lrStartEnd.getFirst(),lrStartEnd.getSecond(), nEpisodes),
        new LogarithmicDecay(probStartEnd.getFirst(),probStartEnd.getSecond(), nEpisodes)
        );
    }

    public static  LrAndProbRandDecay of(TrainerGridParameters trainerParameters) {
        return of(
                trainerParameters.learningRateStartAndEnd(),
                trainerParameters.probRandomActionStartAndEnd(),
                trainerParameters.nEpisodes());
    }
}
