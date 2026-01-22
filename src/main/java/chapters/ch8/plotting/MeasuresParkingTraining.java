package chapters.ch8.plotting;

import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.trainer.core.TrainingStats;
import lombok.Builder;

@Builder
public record MeasuresParkingTraining(
        double step,
        double sumRewards,
        double rewardAverage,
        double nOoccupAvg) {


    public static MeasuresParkingTraining getMeasures(StateParking s, TrainingStats stats) {
        return MeasuresParkingTraining.builder()
                .step(s.nSteps())
                .sumRewards(stats.rewardSum())
                .rewardAverage(stats.rewardAverage())
                .nOoccupAvg(stats.meanNofOccup())
                .build();
    }

}
