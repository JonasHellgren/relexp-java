package chapters.ch14.environments.pong;

import core.foundation.util.formatting.NumberFormatterUtil;
import core.foundation.util.unit_converter.MyUnitConverter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;


/**
 * This class represents the complete (markovian) state of the Pong game.
 * It includes the position of the ball, the velocity of the ball, the position of the paddle,
 * the angle of the paddle, the time the paddle has been still, and the game's finite state.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatePong {

    public static final double PADDLE_Y = 0d;
    PosXy posBall;
    VelocityXy velocityBall;
    PosXy posPaddle;
    double headingAngle;
    double timeStill;
    FiniteState finiteState;

    public static StatePong of(PosXy posBall,
                               double posPaddleX,
                               double headingAngle,
                               double tStill) {
        return StatePong.of(posBall,
                VelocityXy.zero(),
                posPaddleX,
                headingAngle,
                tStill,
                FiniteState.empty());
    }

    //TODO: make private
    public static StatePong of(PosXy posBall,
                               VelocityXy velocityBall,
                               double posPaddleX,
                               double headingAngle,
                               double tStill) {
        return StatePong.of(posBall,
                velocityBall,
                posPaddleX,
                headingAngle,
                tStill,
                FiniteState.empty());
    }

    //TODO: make private
    public static StatePong of(PosXy posBall,
                               VelocityXy velocityBall,
                               double posPaddleX,
                               double headingAngle,
                               double tStill,
                               FiniteState finiteState) {
        return new StatePong(posBall,
                velocityBall,
                PosXy.of(posPaddleX, PADDLE_Y),
                headingAngle,
                tStill,
                finiteState);
    }

    public static StatePong empty() {
        return new StatePong(PosXy.zero(), VelocityXy.zero(), PosXy.zero(), 0d, 0d, FiniteState.empty());
    }

    public PosXy posBall() {
        return posBall;
    }

    public VelocityXy velBall() {
        return velocityBall;
    }

    public PosXy posPaddle() {
        return posPaddle;
    }

    public double headingAngle() {
        return headingAngle;
    }

    public double timeStill() {
        return timeStill;
    }

    public double deltaX() {
        return posBall.x() - posPaddle.x();
    }

    public FiniteState finiteState() {
        return finiteState;
    }

    public PosXy newPosBall(VelocityXy velBall, double timeStep) {
        return new PosXy(posBall().x() + velBall.x() * timeStep, posBall().y() + velBall.y() * timeStep);
    }


    public PosXy newXPosPaddle(ActionPong action, double v) {
        return PosXy.of(posPaddle().x() + action.speedPaddleX * v, posPaddle().y());
    }


    public boolean isOkPosBallAndPaddle(PongSettings settings) {
        return !posBall().isXExceeds(settings.xMax()) &&
                !posBall().yExceeds(settings.yMax()) &&
                !posPaddle().isXExceeds(settings.xMaxMinusHalfPaddle());
    }

    public StatePong copy() {
        return new StatePong(posBall, velocityBall, posPaddle, headingAngle, timeStill, finiteState);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatePong that = (StatePong) o;
        return posBall().equals(that.posBall())
                && velocityBall.equals(that.velocityBall)
                && posPaddle.equals(that.posPaddle)
                && Double.compare(that.headingAngle, headingAngle) == 0
                && Double.compare(that.timeStill, timeStill) == 0
                && finiteState.equals(that.finiteState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(posBall, velocityBall, posPaddle, headingAngle, timeStill, finiteState);
    }

    @Override
    public String toString() {
        return "posBall=" + posBall() +
                ", posPaddle=" + rounded(posPaddle().x()) +
                ", headingAngle=" + rounded(MyUnitConverter.convertRadiansToDegrees(headingAngle)) +
                ", timeStill=" + rounded(timeStill) +
                System.lineSeparator() +
                ", finiteState=" + finiteState;
    }

    @NotNull
    private String rounded(double num) {
        return NumberFormatterUtil.getRoundedNumberAsString(num, 2);
    }


}
