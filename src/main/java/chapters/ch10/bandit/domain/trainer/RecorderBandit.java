package chapters.ch10.bandit.domain.trainer;

import chapters.ch10.plotting.MeasuresBandit;
import chapters.ch10.plotting.MeasuresBanditEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * A recorder for storing and managing a list of MeasuresBandit objects.
 * Enables to plot and save the trajectory of the measures.
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecorderBandit {

    List<MeasuresBandit> measures;

    public static RecorderBandit empty() {
        return new RecorderBandit(new ArrayList<>());
    }

    public void add(MeasuresBandit measuresBandit) {
        measures.add(measuresBandit);
    }

    public boolean isEmpty() {
        return measures.isEmpty();
    }

    public List<Double> trajectory(MeasuresBanditEnum measure) {
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
            sb.append("G=").append(m.sumRewards())
                    .append(", pL=").append(m.probLeft())
                    .append(", glL=").append(m.gradLogLeft())
                    .append("\n");
        }
        return sb.toString();
    }

}
