package chapters.ch10.plotting;

import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
public enum MeasuresBanditEnum {
    SUMREWARDS("Return", pm -> pm.sumRewards()),
    PROBLEFT("Probability left", pm -> pm.probLeft()),
    PROBRIGHT("Probability right", pm -> 1 - pm.probRight()),
    GRADLOGLEFT("Grad. log left", pm -> pm.gradLogLeft()),
    GRADLOGRIGHT("Grad. log right", pm -> 1 - pm.gradLogRight());

    public final String description;
    public final Function<MeasuresBandit, Double> mapFunction;

}
