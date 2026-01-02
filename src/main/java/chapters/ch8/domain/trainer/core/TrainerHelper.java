package chapters.ch8.domain.trainer.core;

import chapters.ch8.domain.environment.core.StepReturnParking;
import core.foundation.util.math.Accumulator;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

@AllArgsConstructor
public class TrainerHelper {
    boolean termState;
    Accumulator accum;
    DescriptiveStatistics stats;


    public static TrainerHelper empty() {
        return new TrainerHelper(false, Accumulator.empty(), new DescriptiveStatistics());
    }
    public void update(StepReturnParking sr, int nOccupied) {
        termState = sr.isTerminal();
        accum.add(sr.reward());
        stats.addValue(nOccupied);
    }
}
