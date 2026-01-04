package chapters.ch13.environments.jumper;

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

    public static final int MAX_HEIGHT = 3;
    static final int MAX_POS = 3;
    public static final double REWARD_COIN = 1;
    static final double REWARD_NOT_COIN = 0.0;

    /**
     * List of positions with coins.
     */
    static final List<StateJumper> POS_WITH_COINS = List.of(
            StateJumper.of(1, 1),
            StateJumper.of(2, 2),
            StateJumper.of(3, 3));

    public static final double REWARD_NOT_UP = 0.0;
    public static final double REWARD_FAIL = -10;

    public static EnvironmentJumper create() {
        return new EnvironmentJumper();
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
        return newState.xPos == MAX_POS || isFail;
    }

    double calcReward(boolean isFail, StateJumper newState) {
        double rewardCoin = POS_WITH_COINS.contains(newState)
                ? REWARD_COIN
                : REWARD_NOT_COIN;
        double rewardFail = isFail ? REWARD_FAIL : 0;
        return rewardCoin + rewardFail;
    }
}
