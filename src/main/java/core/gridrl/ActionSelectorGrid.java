package core.gridrl;

import core.foundation.util.rand.RandUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.util.Map;

/**
 * This class is responsible for selecting actions in a grid environment.
 * It supports both random action selection and action selection based on the maximum value in the memory grid.
 */
@AllArgsConstructor
public class ActionSelectorGrid {

    private final InformerGridParamsI informer;

    public static ActionSelectorGrid of(InformerGridParamsI informerParams) {
        return new ActionSelectorGrid(informerParams);
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
                                                StateActionMemoryGrid memory) {
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
        var validActions= informer.getValidActions();
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
    private ActionGrid chooseActionWithMaxValue(StateGrid state, StateActionMemoryGrid memory) {
        double maxValue = memory.getValueOfBestAction(state);
        Map<ActionGrid, Double> actionValueMap = memory.readActionValuesInState(state);
        return informer.getValidActions().stream()
                .filter(action -> actionValueMap.get(action) == maxValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No best action found"));
    }

}