package chapters.ch12.inv_pendulum.plotting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecorderTrainerPendulum {

    List<MeasuresPendulumTraining> measuresList;

    public static RecorderTrainerPendulum empty() {
        return new RecorderTrainerPendulum(new ArrayList<>());
    }

    public void add(MeasuresPendulumTraining measures) {
        measuresList.add(measures);
    }


    public boolean isEmpty() {
        return measuresList.isEmpty();
    }

    public List<Double> trajectory(MeasuresPendulumTrainingEnum measure) {
        return measuresList.stream().map(measure.mapFunction).toList();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        measuresList.forEach(m -> sb.append(m.toString()).append("\n"));
        return sb.toString();
    }

}
