package core.plotting_rl.progress_plotting;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;


/**
 * Represents a recorder for training progress.
 * It stores a list of progress measures and provides methods to add, clear, and retrieve the measures.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecorderProgressMeasures {

    List<ProgressMeasures> measuresList;

    public static RecorderProgressMeasures empty() {
        return new RecorderProgressMeasures(new ArrayList<>());
    }

    public void add(ProgressMeasures pm) {
        measuresList.add(pm);
    }

    public void clear() {
        measuresList.clear();
    }

    public boolean isEmpty() {
        return measuresList.isEmpty();
    }

    public List<Double> trajectory(ProgressMeasureEnum measure) {
        var functionHashMap = getStringFunctionHashMap();
        Preconditions.checkArgument(functionHashMap.containsKey(measure), "Unknown name: " + measure);
        return measuresList.stream().map((functionHashMap.get(measure))).toList();
    }

    private static HashMap<ProgressMeasureEnum, Function<ProgressMeasures, Double>> getStringFunctionHashMap() {
        HashMap<ProgressMeasureEnum, Function<ProgressMeasures, Double>> map=new HashMap<>();
        map.put(ProgressMeasureEnum.RETURN,pm -> pm.sumRewards());
        map.put(ProgressMeasureEnum.N_STEPS,pm -> (double) pm.nSteps());
        map.put(ProgressMeasureEnum.TD_ERROR,ProgressMeasures::tdError);
        map.put(ProgressMeasureEnum.TD_ERROR_CLIPPED,ProgressMeasures::tdErrorClipped);
        map.put(ProgressMeasureEnum.GRADIENT_ACTOR_MEAN,ProgressMeasures::gradMean);
        map.put(ProgressMeasureEnum.GRADIENT_ACTOR_MEAN_CLIPPED,ProgressMeasures::gradMeanClipped);
        map.put(ProgressMeasureEnum.STD_ACTOR,ProgressMeasures::stdActor);
        map.put(ProgressMeasureEnum.TD_BEST_ACTION,ProgressMeasures::tdBestAction);
        map.put(ProgressMeasureEnum.SIZE_MEMORY,pm -> (double) pm.nMemory());
        return map;
    }

}
