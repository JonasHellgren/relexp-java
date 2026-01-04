package chapters.ch2.domain.parameter_fitting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record LearningRateFittingResults(Map<Double, List<Double>> map) {
    public static LearningRateFittingResults empty() {
        return new LearningRateFittingResults(new HashMap<>());
    }

    public void add(double learningRate, List<Double> outputs) {
        map.put(learningRate, outputs);
    }

    public List<Double> getOutputs(double learningRate) {
        return map.get(learningRate);
    }
}
