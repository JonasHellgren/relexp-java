package chapters.ch5.implem.walk;

import chapters.ch5.domain.environment.ParametersMcI;
import chapters.ch5.domain.environment.StateMcI;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.Getter;
import java.util.List;
import java.util.Set;

@Builder
@Getter
public class RandomWalkParameters implements ParametersMcI {

    public static final double R_FAIL = -1;
    public static final double R_TERMINAL_NON_FAIL = 1.0;
    public static final List<Integer> NON_TERMINAL_POSITIONS= List.of(2,3,4);
    Set<StateWalk> terminalNonFailsStates;
    Set<StateWalk> failStates;
    int xMin;
    int xMax;

    @Override
    public boolean isTerminalNonFail(StateMcI state) {
        return terminalNonFailsStates.contains(getStateWalk(state));
    }

    @Override
    public boolean isFail(StateMcI state) {
        return failStates.contains(getStateWalk(state));
    }

    private static StateWalk getStateWalk(StateMcI state) {
        Preconditions.checkArgument(state instanceof StateWalk);
        return (StateWalk) state;
    }

    public double rewardAtTerminalPos(StateWalk stateWalk) {
        return isFail(stateWalk) ? R_FAIL : R_TERMINAL_NON_FAIL;
    }



}
