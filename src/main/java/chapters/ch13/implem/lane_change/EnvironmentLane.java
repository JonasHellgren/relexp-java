package chapters.ch13.implem.lane_change;

import chapters.ch13.domain.environment.EnvironmentI;
import chapters.ch13.domain.environment.StepReturnI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static core.foundation.util.math.MathUtil.isEqualDoubles;


/***
 * This class represents a lane change environment, where an agent can take actions to change lanes.
 * The environment is defined by a set of settings, including the wheel-base, speed, time max, and
 * reward parameters.
 * Math is described in math_lane_change.md
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EnvironmentLane implements EnvironmentI<StateLane, ActionLane> {

    private final double TINY = 0.01;
    LaneChangeParameters parameters;

    public static EnvironmentLane create(LaneChangeParameters settings) {
        return new EnvironmentLane(settings);
    }

    /**
     * Takes a step in the environment.
     *
     * @param state  the current state
     * @param action the action to take
     * @return the new state, whether the episode is terminal, whether the agent failed, and the reward
     */

    @Override
    public StepReturnI<StateLane> step(StateLane state, ActionLane action) {
        var newState = nextState(state, action);
        boolean isFail = isFail(newState);
        boolean isTerminal = isTerminalState(newState, isFail);
        double reward = calculateReward(isFail, state, newState);
        return new StepReturnI<>(newState, isTerminal, isFail, reward);
    }


    StateLane nextState(StateLane state, ActionLane action) {
        var p = parameters;
        double dt = p.timeStep();
        double headingAngleDot = p.speed() / p.wheelBase() * Math.tan(action.steeringAngle);
        double headingAngle = state.headingAngle() + headingAngleDot * dt;
        double xDot = p.speed() * Math.cos(headingAngle);
        double yDot = p.speed() * Math.sin(headingAngle);
        double x = state.x() + xDot * dt;
        double y = state.y() + yDot * dt;
        double time = state.time() + dt;
        return StateLane.ofWithTime(x, y, headingAngleDot, headingAngle, time);
    }


    // Check if the agent is above or below road limit
    boolean isFail(StateLane state) {
        return state.y() > 0 || state.y() < parameters.yPosDitch();
    }

    boolean isTerminalState(StateLane state, boolean isFail) {
        return state.time() >= parameters.timeMax() || isFail;
    }

    /**
     * It is desirable to avoid changes in steering angle during. The previous steering angle is
     * not known, therefore using the change in heading angle time derivative.
     * Heading angle time derivative is in proportion to the steering angle.
     */
    double calculateReward(boolean isFail, StateLane oldS, StateLane newS) {
        var p = parameters;
        double rewardHeadingDotChange = isSameHeadingTimeDerivate(oldS, newS)
                ? 0
                : p.rChangeSteering();
        double rewardCorrectYPos = p.isDesiredYPositionReached(newS)
                ? p.rCorrectYPos()
                : 0;
        double rewardFail = isFail
                ? p.rFail()
                : 0;
        return rewardHeadingDotChange + rewardCorrectYPos + rewardFail;
    }

    private  boolean isSameHeadingTimeDerivate(StateLane oldS, StateLane newS) {
        return isEqualDoubles(oldS.headingAngleDot(), newS.headingAngleDot(), TINY);
    }


}
