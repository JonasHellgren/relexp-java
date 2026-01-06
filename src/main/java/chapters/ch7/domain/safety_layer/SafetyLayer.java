package chapters.ch7.domain.safety_layer;

import core.gridrl.StateActionGrid;
import core.gridrl.TrainerGridDependencies;
import core.foundation.util.rand.RandUtils;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * A safety layer that prevents the agent from taking actions that have led to failures in the past.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SafetyLayer {

    private final FailStateActionMemory failMemory;

    public static SafetyLayer of(FailStateActionMemory failMemory) {
        return new SafetyLayer(failMemory);
    }

    public static SafetyLayer empty(TrainerGridDependencies dependencies) {
        return of(FailStateActionMemory.empty(dependencies));
    }

    /**
     * Adds a failed state-action pair to the memory.
     *
     * @param sa the state-action pair to add
     */
    public void add(StateActionGrid sa) {
        failMemory.add(sa);
    }

    /**
     * Returns the size of the fail memory.
     *
     * @return the size of the fail memory
     */
    public int memorySize() {
        return failMemory.size();
    }

    /**
     * Corrects the given action based on the fail memory.
     * If the state-action pair is in the fail memory, returns a random non-failing action.
     * Otherwise, returns the original action.
     *
     * @param s the current state
     * @param a the action to correct
     * @return the corrected action
     */
    public ActionGrid correct(StateGrid s, ActionGrid a) {
        var sa= StateActionGrid.of(s, a);
        if (failMemory.contains(sa)) {
            List<ActionGrid> nonFailActions = failMemory.getNonFailActions(s);
            int randIdx= RandUtils.getRandomIntNumber(0, nonFailActions.size());
            return nonFailActions.isEmpty()
                    ? a
                    : nonFailActions.get(randIdx);
        } else {
            return a;
        }

    }

    @Override
    public String toString() {
        return failMemory.toString();
    }

}
