package chapters.ch12.plotting_bandit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BanditNeuralRecorder {

    List<MeasuresBanditNeural> measuresList;

    public static BanditNeuralRecorder empty() {
        return new BanditNeuralRecorder(new ArrayList<>());
    }

    public void add(MeasuresBanditNeural measures) {
        measuresList.add(measures);
    }

    public boolean isEmpty() {
        return measuresList.isEmpty();
    }

    public List<Double> trajectory(MeasuresBanditNeuralEnum measure) {
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
