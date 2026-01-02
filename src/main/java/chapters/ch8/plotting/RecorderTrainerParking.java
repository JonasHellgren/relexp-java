package chapters.ch8.plotting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecorderTrainerParking {

    List<MeasuresParkingTraining> measuresList;

    public static RecorderTrainerParking empty() {
        return new RecorderTrainerParking(new ArrayList<>());
    }

    public void add(MeasuresParkingTraining measures) {
        measuresList.add(measures);
    }

    public boolean isEmpty() {
        return measuresList.isEmpty();
    }

    public List<Double> trajectory(MeasuresParkingTrainingEnum measure) {
        return measuresList.stream().map(measure.mapFunction).toList();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        measuresList.forEach(m -> sb.append(m.toString()).append("\n"));
        return sb.toString();
    }



}
