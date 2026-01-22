package chapters.ch14.environments.pong;

import chapters.ch14.domain.environment.EnvironmentI;
import chapters.ch14.domain.environment.StepReturn;
import core.foundation.util.math.MathUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * Check hits for the new psi using the current ball position and the post-action paddle position.
 * After computing new psi, we advance the ball and do terminal/miss checks based on
 * new ball position and the post-action paddle position
 */

@AllArgsConstructor
@Getter
public class EnvironmentPong implements EnvironmentI<StatePong, ActionPong> {
    PongSettings settings;

    public static EnvironmentPong of(PongSettings settings) {
        return new EnvironmentPong(settings);
    }

    @Override
    public StepReturn<StatePong> step(StatePong state, ActionPong action) {
        PosXy newPosPaddle0 = state.newXPosPaddle(action, settings.timeStep());
        PosXy newPosPaddle= clipXForPaddle(newPosPaddle0);
        var fsBefore=FiniteState.of(state.posBall, newPosPaddle, settings);
        double newPsi = newPsi(state, fsBefore);
        VelocityXy newVelBall = VelocityXy.of(settings.speedBall(), newPsi);
        PosXy newPosBall = state.newPosBall(newVelBall,settings.timeStep());
        newPosBall = clipXAndYForBall(newPosBall);
        var fsAfter=FiniteState.of(newPosBall, newPosPaddle, settings);
        double newTimeStill = calculateNewTimeStill(state, action, fsAfter);
        var newState = StatePong.of(
                newPosBall,
                newVelBall,
                newPosPaddle.x(),
                newPsi,
                newTimeStill,
                fsAfter);
        boolean isFail = fsAfter.isBallLost();
        double reward=calculateReward(state,action,fsAfter,isFail);

        return StepReturn.<StatePong>builder()
                .stateNew(newState)
                .isTerminal(isFail)
                .isFail(isFail)
                .reward(reward)
                .build();
    }

    private PosXy clipXForPaddle(PosXy pos) {
        return PosXy.of(MathUtil.clip(
                pos.x(),settings.halfPaddleLength(),settings.xMaxMinusHalfPaddle()),
                pos.y());
    }

    private PosXy clipXAndYForBall(PosXy newPosBall) {
        return PosXy.of(
                MathUtil.clip(newPosBall.x(),settings.xMinPlusHalfRadiusBall(),settings.xMaxMinusHalfRadiusBall()),
                MathUtil.clip(newPosBall.y(),settings.yMinPlusHalfRadiusBall(),settings.yMaxMinusHalfRadiusBall()));
    }


    private double newPsi(StatePong state, FiniteState finiteState) {
        final double psi = state.headingAngle();
        final boolean hitV = finiteState.isHitVert(); // side wall
        final boolean hitH = finiteState.isHitHor();  // floor/ceiling

        if (hitV && hitH) return wrapToPi(psi + Math.PI); // corner: flip both components
        if (hitV)        return wrapToPi(Math.PI - psi);  // vertical reflection
        if (hitH)        return wrapToPi(-psi);           // horizontal reflection
        return wrapToPi(psi);
    }

    // Keep angles in a stable range to avoid drift in tests/visuals.
    private static double wrapToPi(double a) {
        double twoPi = 2.0 * Math.PI;
        double x = ((a + Math.PI) % twoPi + twoPi) % twoPi - Math.PI;
        return (x <= -Math.PI) ? x + twoPi : x;
    }

    private double calculateNewTimeStill(StatePong state, ActionPong action, FiniteState finiteState) {
        return finiteState.isBallHitPaddle() || action.isMove()
                ? 0
                : state.timeStill + settings.timeStep();
    }

    private double calculateReward(StatePong state,
                                   ActionPong action,
                                   FiniteState finiteState,
                                   boolean isFail) {
        boolean isMove = action.isMove();
        double reward = 0;
        if (finiteState.isBallHitPaddle()) reward += state.timeStill();
        if (isMove) reward += settings.penMove();
        if (isFail) reward += settings.penFail();
        return reward;
    }

}
