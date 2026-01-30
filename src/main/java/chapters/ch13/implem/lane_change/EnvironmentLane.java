package chapters.ch13.implem.lane_change;

import chapters.ch13.domain.environment.EnvironmentI;
import chapters.ch13.domain.environment.StepReturnI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    public static final double TINY = 0.01;

    @Builder
    public record Settings(double wheelBase,
                           double speed,  //m/s
                           double timeMax,  //s
                           double yPosDesired, //m
                           double yPosDesiredMargin, //m
                           double yPosDitch, //m
                           double rFail,
                           double rChangeSteering,
                           double rCorrectYPos,
                           double timeStep) {
    }

    Settings settings;

    public static EnvironmentLane create() {
        return create(defaultSettings());
    }

    public static EnvironmentLane create(Settings settings) {
        return new EnvironmentLane(settings);
    }

    private static Settings defaultSettings() {
        return Settings.builder()
                .wheelBase(2.5).speed(20)
                .timeMax(3)
                .yPosDesired(-3).yPosDesiredMargin(0.5).yPosDitch(-5)
                .rFail(-10).rChangeSteering(-1.0).rCorrectYPos(1)
                .timeStep(0.25)
                .build();
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
        var s = settings;
        double headingAngleDot = s.speed / s.wheelBase * Math.tan(action.steeringAngle);
        double headingAngle = state.headingAngle() + headingAngleDot * s.timeStep;
        double xDot = s.speed * Math.cos(headingAngle);
        double yDot = s.speed * Math.sin(headingAngle);
        double x = state.x() + xDot * s.timeStep;
        double y = state.y() + yDot * s.timeStep;
        double time = state.time() + s.timeStep;
        return StateLane.ofWithTime(x, y,headingAngleDot, headingAngle, time);
    }


    // Check if the agent is above or below road limit
    boolean isFail(StateLane state) {
        return state.y() > 0 || state.y() < settings.yPosDitch;
    }

    boolean isTerminalState(StateLane state, boolean isFail) {
        return state.time() >= settings.timeMax || isFail;
    }

    /**
     * It is desirable to avoid changes in steering angle during. The previous steering angle is
     * not known, therefore using the change in heading angle time derivative.
     * Heading angle time derivative is in proportion to the steering angle.
     */
    double calculateReward(boolean isFail, StateLane oldS, StateLane newS) {
        var s = settings;
        double rewardHeadingDotChange = isSameHeadingTimeDerivate(oldS, newS)
                ? 0
                : s.rChangeSteering;
        double rewardCorrectYPos = isDesiredYPositionReached(newS, s)
                ? s.rCorrectYPos
                : 0;
        double rewardFail = isFail
                ? s.rFail
                : 0;
        return rewardHeadingDotChange + rewardCorrectYPos + rewardFail;
    }

    private static boolean isDesiredYPositionReached(StateLane newS, Settings s) {
        return isEqualDoubles(newS.y(), s.yPosDesired, s.yPosDesiredMargin);
    }

    private static boolean isSameHeadingTimeDerivate(StateLane oldS, StateLane newS) {
        return isEqualDoubles(oldS.headingAngleDot(), newS.headingAngleDot(), TINY);
    }


}
