package chapters.ch14.plotting;

import chapters.ch14.domain.interfaces.RecorderI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Recorder implements RecorderI {

    List<MeasuresCombLP> measures;

    public static Recorder empty() {
        return new Recorder(new ArrayList<>());
    }

    @Override
    public boolean isEmpty() {
        return measures.isEmpty();
    }

    @Override
    public void add(MeasuresCombLP measures) {
        this.measures.add(measures);
    }

    @Override
    public List<Double> trajectory(MeasuresCombLPEnum measure) {
        return measures.stream().map(measure.mapFunction).toList();
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        for (var m : measures) {
            sb.append("G=").append(m.sumRewards())
                    .append(", count=").append(m.count())
                    .append(", sumRewards=").append(m.sumRewards())
                    .append("\n");
        }
        return sb.toString();
    }


}
