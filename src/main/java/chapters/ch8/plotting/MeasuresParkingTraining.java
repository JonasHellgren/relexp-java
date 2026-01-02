package chapters.ch8.plotting;

import lombok.Builder;

@Builder
public record MeasuresParkingTraining(
        double step,
        double sumRewards,
        double rewardAverage,
        double nOoccupAvg) {

}
