package chapters.ch5.implem.walk;

import chapters.ch5.domain.environment.*;
import com.google.common.base.Preconditions;
import core.foundation.util.math.MathUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentWalk implements EnvironmentMcI {

    public static final String NAME = "walk";
    public static final double NON_TERM_REWARD = 0;

    RandomWalkParameters parameters;

    public static EnvironmentWalk of(ParametersMcI parameters) {
        Preconditions.checkArgument(parameters instanceof RandomWalkParameters,
                "invalid class of parameters=" + parameters);
        return new EnvironmentWalk((RandomWalkParameters) parameters);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public StepReturnMc step(StateMcI state, ActionMcI action) {
        Preconditions.checkArgument(state instanceof StateWalk, "invalid class of state=" + state);
        Preconditions.checkArgument(action instanceof ActionWalk, "invalid class of action=" + action);
        var stateWalk = (StateWalk) state;
        var actionWalk = (ActionWalk) action;
        int xNext= MathUtil.clip(stateWalk.x() + actionWalk.deltaX(),parameters.getXMin(),parameters.getXMax());
        var stateNext = StateWalk.of(xNext);
        boolean isFail = parameters.isFail(stateNext);
        boolean isTerminal = parameters.isTerminalNonFail(stateNext) || isFail;
        double reward = isTerminal ? parameters.rewardAtTerminalPos(stateNext) : NON_TERM_REWARD;
        return StepReturnMc.builder()
                .sNext(stateNext)
                .reward(reward)
                .isFail(isFail)
                .isTerminal(isTerminal)
                .build();
    }

}
