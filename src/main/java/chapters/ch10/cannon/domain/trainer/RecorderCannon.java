package chapters.ch10.cannon.domain.trainer;

import chapters.ch10.plotting.MeasuresCannon;
import chapters.ch10.plotting.MeasuresCannonEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * A recorder for storing and managing a list of MeasuresCannon objects.
 * Enables to plot and save the trajectory of the measures.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecorderCannon {

    List<MeasuresCannon> measures;

    public static RecorderCannon empty() {
        return new RecorderCannon(new ArrayList<>());
    }

    public void add(MeasuresCannon measuresCannon) {
        measures.add(measuresCannon);
    }

    public boolean isEmpty() {
        return measures.isEmpty();
    }

    public List<Double> trajectory(MeasuresCannonEnum measure) {
        return measures.stream().map(measure.mapFunction).toList();
    }

    public int size() {
        return measures.size();
    }

    public void clear() {
        measures.clear();
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        for (var m : measures) {
            sb.append("G=").append(m.returnMinusBase())
                    .append(", m=").append(m.mean())
                    .append(", s=").append(m.std())
                    .append("\n");
        }
        return sb.toString();

    }


}
