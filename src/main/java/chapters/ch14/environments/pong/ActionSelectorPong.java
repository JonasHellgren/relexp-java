package chapters.ch14.environments.pong;

import chapters.ch14.domain.action_selector.ActionSelectorI;
import chapters.ch14.domain.settings.PlanningSettings;
import core.foundation.util.rand.RandUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ActionSelectorPong class.
 * This class implements an action selector for the Pong game.
 * It selects actions based on the probability of changing actions.
 * Some heuristics are used to speed up, sequence with both left and right actions is not allowed.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionSelectorPong implements ActionSelectorI<ActionPong> {
    private static final int N_ACTIONS = 2;

    private double probChange;

    public static ActionSelectorPong of(PlanningSettings settings) {
        return new ActionSelectorPong(settings.probActionChange());
    }


    public static ActionSelectorPong of(double probActionChange) {
        return new ActionSelectorPong(probActionChange);
    }

    @Override
    public List<ActionPong> selectActions(int depth) {
        var possibleActions = getPossibleActions();
        return getActionPongList(depth, possibleActions);
    }

    /**
     * Gets a list of possible actions that do not include both left and right.
     * left, left, still: ok
     * left, still, left: ok
     * left, right, still: not ok
     * @return A list of ActionPong instances.
     */

    private static List<ActionPong> getPossibleActions() {
        var actions = ActionPong.actionsMutable();
        List<ActionPong> selectedItems = new ArrayList<>();
        boolean isLeftAndRightActions = true;
        while (isLeftAndRightActions) {
            Collections.shuffle(actions);
            selectedItems = actions.subList(0, N_ACTIONS);
            isLeftAndRightActions = selectedItems.contains(ActionPong.left)
                    && selectedItems.contains(ActionPong.right);
        }
        return selectedItems;
    }

    @NotNull
    private List<ActionPong> getActionPongList(int depth, List<ActionPong> selectedItems) {
        var currentAction = selectedItems.get(RandUtil.getRandomIntNumber(0, N_ACTIONS - 1));
        List<ActionPong> newList = new ArrayList<>();
        newList.add(currentAction);
        for (int j = 0; j < depth-1; j++) {
            currentAction = RandUtil.randomNumberBetweenZeroAndOne() < probChange
                    ? getDifferentItem(currentAction, selectedItems)
                    : currentAction;
            newList.add(currentAction);
        }
        return newList;
    }

    private ActionPong getDifferentItem(ActionPong item, List<ActionPong> list) {
        return list.stream()
                .filter(i -> !i.equals(item))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No different item found"));
    }

}
