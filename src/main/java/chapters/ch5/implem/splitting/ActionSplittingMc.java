package chapters.ch5.implem.splitting;

import chapters.ch5.domain.environment.ActionMcI;
import core.gridrl.ActionGrid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionSplittingMc implements ActionMcI {

    ActionGrid action;

    public static ActionSplittingMc of(ActionGrid action) {
        return new ActionSplittingMc(action);
    }

    public static ActionMcI valueOf(String string) {
        return new ActionSplittingMc(ActionGrid.valueOf(string));
    }

    @Override
    public String toString() {
        return action.toString();
    }


}
