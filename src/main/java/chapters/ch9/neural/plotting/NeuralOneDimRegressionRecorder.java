package chapters.ch9.neural.plotting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NeuralOneDimRegressionRecorder {

    List<MeasuresOneDimRegressionNeural> measuresList;

    public static NeuralOneDimRegressionRecorder empty() {
        return new NeuralOneDimRegressionRecorder(new ArrayList<>());
    }

    public void add(MeasuresOneDimRegressionNeural measures) {
        measuresList.add(measures);
    }

    public boolean isEmpty() {
        return measuresList.isEmpty();
    }

    public List<Double> trajectory(MeasuresOneDimRegressionNeuralEnum measure) {
        return measuresList.stream().map(measure.mapFunction).toList();
    }

    public int size() {
        return measuresList.size();
    }

    public void clear() {
        measuresList.clear();
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        for (var m : measuresList) {
            sb.append("error=").append(m.error())
                    .append("\n");
        }
        return sb.toString();

    }


}
