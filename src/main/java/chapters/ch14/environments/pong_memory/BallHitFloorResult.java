package chapters.ch14.environments.pong_memory;

import lombok.Builder;

/***
 * Result from BallHitFloorCalculator
 */

@Builder
public record BallHitFloorResult(
        double timeHit,
        double xBall
) {

    public static BallHitFloorResult of(double timeHit, double xBall) {
        return new BallHitFloorResult(timeHit, xBall);
    }

}
