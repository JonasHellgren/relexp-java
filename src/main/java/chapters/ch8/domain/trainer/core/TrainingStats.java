package chapters.ch8.domain.trainer.core;

import core.foundation.gadget.math.Accumulator;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

@AllArgsConstructor
public class TrainingStats {
    Accumulator rewardAccumulator;
    Accumulator rewardAverageAccumulator;
    DescriptiveStatistics statsNofOccup;

    public static TrainingStats empty() {
        return new TrainingStats(Accumulator.empty(),Accumulator.empty(), new DescriptiveStatistics());
    }
    public void update(double reward, double deltaAvgReward, int nOccupied) {
        rewardAccumulator.add(reward);
        rewardAverageAccumulator.add(deltaAvgReward);
        statsNofOccup.addValue(nOccupied);
    }

    public double rewardAverage() {
        return rewardAverageAccumulator.value();
    }

    public double rewardSum() {
        return rewardAccumulator.value();
    }

    public double meanNofOccup() {
        return statsNofOccup.getMean();
    }
}
