package chapters.ch12.plotting_bandit;

import lombok.AllArgsConstructor;
import java.util.function.Function;

@AllArgsConstructor
public enum MeasuresBanditNeuralEnum {
    LOSS("Loss", pm -> pm.error()),
    VALUE_LEFT("Action value left", pm -> pm.valueLeft()),
    VALUE_RIGHT("Action value right", pm -> pm.valueRight());

    public final String description;
    public final Function<MeasuresBanditNeural, Double> mapFunction;

}
