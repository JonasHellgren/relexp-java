package chapters.ch6.domain.trainer_dep.result_generator;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Represents a collection of multi-step results, where each result corresponds to a specific step.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiStepResultsGrid {

    private final List<MultiStepResultGrid> results;

    public static MultiStepResultsGrid create(int nSteps) {
        return new MultiStepResultsGrid(Lists.newArrayListWithCapacity(nSteps));
    }

    public int size() {
        return results.size();
    }

    public void add(MultiStepResultGrid result) {
        results.add(result);
    }

    public MultiStepResultGrid resultAtStep(int step) {
        return results.get(step);
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        String lineSep = System.lineSeparator();
        sb.append("nExperiences=").append(size()).append(lineSep);
        for (MultiStepResultGrid resultItem:results) {
            sb.append(resultItem).append(lineSep);
        }
        return sb.toString();
    }

}
