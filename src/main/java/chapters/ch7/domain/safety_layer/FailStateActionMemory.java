package chapters.ch7.domain.safety_layer;

import core.gridrl.StateActionGrid;
import core.gridrl.TrainerGridDependencies;
import core.foundation.util.cond.ConditionalsUtil;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a memory of failed state-action pairs.
 * This class provides methods to add, retrieve, and check for failed state-action pairs.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class FailStateActionMemory {

    TrainerGridDependencies dependencies;
    Set<StateActionGrid> failMemory;

    public static FailStateActionMemory of(TrainerGridDependencies dependencies, Set<StateActionGrid> failMemory) {
        return new FailStateActionMemory(dependencies, failMemory);
    }

    public static FailStateActionMemory empty(TrainerGridDependencies dependencies) {
        return of(dependencies, new HashSet<>());
    }

    public boolean contains(StateActionGrid sa) {
        return failMemory.contains(sa);
    }

    public void add(StateActionGrid sa) {
        failMemory.add(sa);
    }

    public int size() {
        return failMemory.size();
    }

    public List<ActionGrid> getNonFailActions(StateGrid s) {
        List<ActionGrid> validActions = new ArrayList<>(
                dependencies.environment().informer().getValidActions());
        for (StateActionGrid sa : failMemory) {
            if (sa.state().equals(s)) {
                validActions.remove(sa.action());
            }
        }
        ConditionalsUtil.executeIfTrue(validActions.isEmpty(), () ->
                log.info("No valid actions found for state: " + s));
        return validActions;
    }

    @Override
    public String toString() {
        var sb=new StringBuilder();
        sb.append("Fail memory:\n");
        for (StateActionGrid sa : failMemory) {
            sb.append(sa.toString()).append("\n");
        }
        return sb.toString();
    }


}
