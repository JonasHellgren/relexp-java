package chapters.ch4.domain.helper;

import chapters.ch4.domain.memory.MemoryGrid;
import core.foundation.util.rand.RandUtils;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.util.Map;

/**
 * This class is responsible for selecting actions in a grid environment.
 * It supports both random action selection and action selection based on the maximum value in the memory grid.
 */
@AllArgsConstructor
public class ActionSelectorGrid {

    private final EnvironmentGridParametersI gridParameters;

    public static ActionSelectorGrid of(EnvironmentGridParametersI gridParameters) {
        return new ActionSelectorGrid(gridParameters);
    }

    /**
     * Chooses an action using an epsilon-greedy strategy.
     * With probability probRandom, a random action is chosen. Otherwise, the action with the maximum value in the memory grid is chosen.
     *
     * @param state The current state of the environment.
     * @param probRandom The probability of choosing a random action.
     * @param memory The memory grid containing state-action values.
     * @return The chosen action.
     */
    public ActionGrid chooseActionEpsilonGreedy(StateGrid state,
                                                double probRandom,
                                                MemoryGrid memory) {
        return (RandomUtils.nextDouble(0, 1) < probRandom)
                ? chooseActionRandom()
                : chooseActionWithMaxValue(state, memory);
    }

    /**
     * Chooses a random action from the set of valid actions.
     *
     * @return The chosen random action.
     */
    private ActionGrid chooseActionRandom() {
        var validActions=gridParameters.getValidActions();
        int randomIndex = RandUtils.getRandomIntNumber(0, validActions.size());
        return validActions.get(randomIndex);
    }

    /**
     * Chooses the action with the maximum value in the memory grid for the given state.
     *
     * @param state The current state of the environment.
     * @param memory The memory grid containing state-action values.
     * @return The chosen action.
     */
    private ActionGrid chooseActionWithMaxValue(StateGrid state, MemoryGrid memory) {
        double maxValue = memory.getValueOfBestAction(state);
        Map<ActionGrid, Double> actionValueMap = memory.readActionValuesInState(state);
        return gridParameters.getValidActions().stream()
                .filter(action -> actionValueMap.get(action) == maxValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No best action found"));
    }

}