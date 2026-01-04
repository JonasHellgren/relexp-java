package chapters.ch14.environments.pong;

import lombok.Builder;

/**
 * This class represents the "health" of the game of Pong.
 * It contains information about the current state of the ball and paddle.
 * The state is determined based on the position of the ball and paddle.
 * isHitVert and isHitHor depend on the ball radius because ball position refers to ball center position.
 * Therefore, the ball is touching wall or paddle when the ball center plus radius hits the wall or paddle
 */

@Builder
public record FiniteState(
        boolean isBallParallelPaddle,
        boolean isHitVert,
        boolean isHitHor,
        boolean isBallHitPaddle,
        boolean isBallLost,
        boolean isBallBottom
) {

    public static FiniteState empty() {
        return new FiniteState(false, false, false, false,false,false);
    }

    public static FiniteState of(PosXy posBall, PosXy posPaddle, PongSettings settings) {
        double deltaX = posBall.x() - posPaddle.x();
        boolean isBallParallelPaddle = Math.abs(deltaX) < settings.lengthPaddle() / 2;
        boolean isHitVert = posBall.xBelowOrEqual(settings.xMinPlusHalfRadiusBall()) ||
                posBall.xAboveOrEqual(settings.xMaxMinusHalfRadiusBall());
        boolean isBallBottom=posBall.yBelowOrEqual(settings.halfRadiusBall());
        boolean isBallHitPaddle =  isBallBottom && isBallParallelPaddle;
        boolean isHitHor = isBallHitPaddle || posBall.yAboveOrEqual(settings.yMaxMinusHalfRadiusBall());

        return FiniteState.builder()
                .isBallParallelPaddle(isBallParallelPaddle)
                .isHitVert(isHitVert)
                .isHitHor(isHitHor)
                .isBallHitPaddle(isBallHitPaddle)
                .isBallLost(isBallBottom && !isBallParallelPaddle)
                .isBallBottom(isBallBottom)
                .build();
    }

}
