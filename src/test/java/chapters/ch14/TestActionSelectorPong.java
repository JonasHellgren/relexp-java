package chapters.ch14;

import chapters.ch14.implem.pong.ActionPong;
import chapters.ch14.implem.pong.ActionSelectorPong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestActionSelectorPong {

    static final int DEPTH = 5;
    ActionSelectorPong actionSelectorPongMixed, actionSelectorHomo;

    @BeforeEach
    void init() {
        actionSelectorPongMixed = ActionSelectorPong.of(0.5);
        actionSelectorHomo = ActionSelectorPong.of(0d);
    }

    @Test
    void testSelectActions_correctSize() {
        var actions = actionSelectorPongMixed.selectActions(DEPTH);
        assertEquals(DEPTH, actions.size());
    }

    @Test
    void testSelectActions_Mixed() {
        var actionsSelected = actionSelectorPongMixed.selectActions(DEPTH);
        int numDistinctElements = getNumDistinctElements(actionsSelected);
        boolean isLeftAndRightActions = actionsSelected.contains(ActionPong.left)
                && actionsSelected.contains(ActionPong.right);
        assertTrue(numDistinctElements == 1 || numDistinctElements == 2);
        assertFalse(isLeftAndRightActions);
    }

    @Test
    void testSelectActions_Homo() {
        var actionsSelected = actionSelectorHomo.selectActions(DEPTH);
        int numDistinctElements = getNumDistinctElements(actionsSelected);
        assertTrue(numDistinctElements == 1);
    }

    private static int getNumDistinctElements(List<ActionPong> actionsSelected) {
        Set<ActionPong> uniqueActions = new HashSet<>(actionsSelected);
        return uniqueActions.size();
    }


}
