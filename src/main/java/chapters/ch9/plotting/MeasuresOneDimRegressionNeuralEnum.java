package chapters.ch9.plotting;

import lombok.AllArgsConstructor;
import java.util.function.Function;

@AllArgsConstructor
public enum MeasuresOneDimRegressionNeuralEnum {
    LOSS("Loss", pm -> pm.error()),
    PRED1("Prediction 1", pm -> pm.valueLeft()),
    PRED10("Prediction 10", pm -> pm.valueRight());

    public final String description;
    public final Function<MeasuresOneDimRegressionNeural, Double> mapFunction;
}
