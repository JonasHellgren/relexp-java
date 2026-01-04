package chapters.ch14.plotting;

import lombok.AllArgsConstructor;
import java.util.function.Function;

@AllArgsConstructor
public enum MeasuresCombLPEnum {
    SUM_REWARDS("Return", pm -> pm.sumRewards()),
    N_STEPS("N. steps", pm -> pm.countAsDouble());

    public final String description;
    public final Function<MeasuresCombLP, Double> mapFunction;
}
