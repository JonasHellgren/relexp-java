package chapters.ch11.domain.trainer.multisteps;

import chapters.ch11.domain.environment.core.StateLunar;
import com.google.common.collect.Lists;
import lombok.Builder;
import java.util.List;

/**
 * Represents the results of a multi-step generation, from class MultiStepResultsGenerator
 * This class contains a list of MultiStepResult objects, each representing a single
 * step in the experience.
 */

@Builder
public class MultiStepResults {

    int nResults;  //equal to length of below lists
    List<MultiStepResult> results;

    public static MultiStepResults create(int nExp) {
        return MultiStepResults.builder()
                .nResults(nExp)
                .results(Lists.newArrayListWithCapacity(nExp))
                .build();
    }


    public int nResults() {
        return nResults;
    }

    public void add(MultiStepResult msr) {
        results.add(msr);
    }

    public boolean isEmpty() {
        return nResults == 0;
    }

    public MultiStepResult experienceAtStep(int step) {
        return results.get(step);
    }

    public boolean isFutureOutsideOrTerminalAtStep(int step) {
        return experienceAtStep(step).isStateFutureTerminalOrNotPresent();
    }

    public StateLunar stateAtStep(int step) {
        return experienceAtStep(step).state();
    }

    public double actionAtStep(int step) {
        return experienceAtStep(step).action();
    }


    public double valueTarAtStep(int step) {
        return experienceAtStep(step).valueTarget();
    }


    public double advantageAtStep(int step) {
        return experienceAtStep(step).advantage();
    }


    public double tdErrorAtStep(int step) {
        return experienceAtStep(step).tdError();
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        String lineSep = System.lineSeparator();
        sb.append("nExperiences=").append(nResults).append(lineSep);
        for (MultiStepResult resultItem:results) {
            sb.append(resultItem).append(lineSep);
        }
        return sb.toString();
    }


}
