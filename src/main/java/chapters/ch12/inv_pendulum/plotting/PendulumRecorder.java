package chapters.ch12.inv_pendulum.plotting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PendulumRecorder {

    List<MeasuresPendulumSimulation> measuresList;

    public static PendulumRecorder empty() {
        return new PendulumRecorder(new ArrayList<>());
    }

    public void add(MeasuresPendulumSimulation measures) {
        measuresList.add(measures);
    }

    public List<Double> trajectory(MeasuresPendulumSimulationEnum measure) {
        return measuresList.stream().map(measure.mapFunction).toList();
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        measuresList.forEach(m -> sb.append(m.toString()).append("\n"));
        return sb.toString();
    }

}
