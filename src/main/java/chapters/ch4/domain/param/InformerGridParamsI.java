package chapters.ch4.domain.param;

import com.google.common.base.Preconditions;
import core.gridrl.ActionGrid;
import core.gridrl.EnvironmentGridParametersI;
import core.gridrl.StateGrid;
import org.apache.commons.math3.util.Pair;

import java.util.List;

public interface InformerGridParamsI {

    String environmentName();

    Pair<Integer, Integer> getPosXMinMax();
    Pair<Integer, Integer> getPosYMinMax();

    List<ActionGrid> getValidActions();
    boolean isTerminalNonFail(StateGrid state);
    boolean isFail(StateGrid state);
    boolean isWall(StateGrid state);
    boolean isValidState(StateGrid state);
    boolean isValidAction(ActionGrid action);
    Double rewardAtTerminalPos(StateGrid state);
    Double rewardMove();

    default boolean isTerminal(StateGrid state) {
        return isTerminalNonFail(state) || isFail(state);
    }

    default void validateStateAndAction(StateGrid s, ActionGrid a) {
        Preconditions.checkArgument(isValidState(s),"invalid state="+ s);
        Preconditions.checkArgument(isValidAction(a),"invalid action="+ a);
    }
    default void validateTerminalState(StateGrid state) {
        boolean terminal = isTerminalNonFail(state) || isFail(state);
        Preconditions.checkArgument(terminal, "invalid state="+ state +", shall be terminal");
    }


}
