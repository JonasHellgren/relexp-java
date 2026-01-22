package chapters.ch14.environments.pong;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.With;

import java.util.List;

import static core.foundation.util.collections.ListCreatorUtil.createFromStartToEndWithNofItems;


/**
 * This class represents the settings for a Pong game.
 * It contains various constants and methods to calculate different values related to the game.
 */
@Builder
@With
public record PongSettings(
        double xMax,
        double yMax,
        double radiusBall,
        double lengthPaddle,
        double speedPaddleMax,
        double speedBall,
        double minHeadingAngleRad,
        double timeStep,
        double penMove,
        double penFail,
        double maxTime
) {

    public static final double X_MIN = 0.0;
    public static final double Y_MIN = 0.0;


    public double xMid() {
        return xMax / 2;
    }

    public double yMid() {
        return yMax / 2;
    }

    public double xMin() {
        return X_MIN;
    }

    public double yMin() {
        return Y_MIN;
    }

    public double xMinPlusHalfPaddle() {
        return  xMin() + halfPaddleLength();
    }

    public double xMaxMinusHalfPaddle() {
        return xMax - halfPaddleLength();
    }

    public double xMinPlusHalfRadiusBall() {
        return xMin() + halfRadiusBall();
    }

    public double xMaxMinusHalfRadiusBall() {
        return xMax - halfRadiusBall();
    }

    public double yMinPlusHalfRadiusBall() {
        return yMin() + halfRadiusBall();
    }

    public double yMaxMinusHalfRadiusBall() {
        return yMax - halfRadiusBall();
    }

    public double halfRadiusBall() {
        return radiusBall() / 2;
    }

    public double halfPaddleLength() {
        return lengthPaddle / 2;
    }

    public double maxHeadingAngleRad() {
        return Math.PI-minHeadingAngleRad();
    }

    public List<Double> timeMaxHitSpace(int nk) {
        return createFromStartToEndWithNofItems(0, timeMaxHitBottom(), nk);
    }

    public List<Double> deltaXSpace(int nk) {
        return createFromStartToEndWithNofItems(0, xMax(), nk);
    }

    public double timeMaxHitBottom() {
        Preconditions.checkArgument(speedBall() > 0, "speedBall must be > 0");
        Preconditions.checkArgument(minHeadingAngleRad() > 0, "minHeadingAngleRad must be > 0");
        double vyMin= speedBall() * Math.sin(minHeadingAngleRad());
        return yMax() / vyMin;
    }

    public double timeMinHitBottom() {
        return 0;
    }

}
