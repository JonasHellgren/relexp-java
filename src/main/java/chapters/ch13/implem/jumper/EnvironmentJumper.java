package chapters.ch13.implem.jumper;

import chapters.ch13.domain.environment.EnvironmentI;
import chapters.ch13.domain.environment.StepReturnI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * Implementation of the Climber environment.
 * This class provides the logic for stepping through the environment,
 * given a state and an action.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentJumper implements EnvironmentI<StateJumper, ActionJumper> {

    private final JumperParameters parameters;

    public static EnvironmentJumper of(JumperParameters parameters) {
        return new EnvironmentJumper(parameters);
    }

    /**
     * Takes a step in the environment, given a state and an action.
     *
     * @param state the current state
     * @param action the action to take
     * @return the result of the step, including the new state, whether the step was terminal,
     * whether the step failed, and the reward
     */
    @Override
    public StepReturnI<StateJumper> step(StateJumper state, ActionJumper action) {
        var newState = nextState(state, action);
        boolean isFail = isFail(state, action);
        boolean isTerminal = isTerminalState(newState, isFail);
        double reward = calcReward(isFail, newState);
        return new StepReturnI<>(newState, isTerminal, isFail, reward);
    }

    StateJumper nextState(StateJumper state, ActionJumper action) {
        return (state.height == 0 && state.xPos > 0)
                ? state.createNextSameHeight()
                : state.createNext(action);
    }

    boolean isFail(StateJumper state, ActionJumper action) {
        return state.height > 0 && action.equals(ActionJumper.n);
    }

    boolean isTerminalState(StateJumper newState, boolean isFail) {
        return newState.xPos == parameters.maxPos() || isFail;
    }

    double calcReward(boolean isFail, StateJumper newState) {
        double rewardCoin = parameters.posWithCoins().contains(newState)
                ? parameters.rewardCoin()
                : parameters.rewardNotCoin();
        double rewardFail = isFail ? parameters.rewardFail() : 0;
        return rewardCoin + rewardFail;
    }
}
