package chapters.ch11.animation;

import chapters.ch11.domain.environment.core.StateLunar;
import core.foundation.util.rand.RandUtil;
import lombok.Builder;
import org.apache.commons.math3.util.Pair;

import java.awt.*;
import java.util.List;


@Builder
record LunarParams(
        StateLunar state,
        Color colorBackground,
        int width,
        int height,
        double armAngle,
        int armLenght,
        int nFireDotsMax,
        int radiusFireDot,
        int radiusFireDotsMax,
        List<Color> colors
) {

    public static LunarParams create(StateLunar state) {
        return LunarParams.builder()
                .state(state)
                .colorBackground(Color.DARK_GRAY)
                .width(2).height(2)
                .armAngle(Math.PI / 4).armLenght(1)
                .nFireDotsMax(300)
                .radiusFireDot(5).radiusFireDotsMax(1)
                .colors(List.of(Color.WHITE, Color.YELLOW))
                .build();
    }

    public static LunarParams empty() {
        return create(StateLunar.zeroPosAndSpeed());
    }


    public Color randomColor() {
        return colors.get(RandUtil.getRandomIntNumber(0, colors.size()));
    }

/*

    public Pair<Integer, Integer> centerCannonFire(double angle) {
        double dist= radiusFireDotsCannon * 2 ;
        double x = cannonEastXPos(angle) + dist * Math.cos(angle);
        double y = cannonEastYPos(angle) + dist * Math.sin(angle);
        return Pair.create((int) x,(int)y);
    }
*/

    public Pair<Double, Double> randomPosInCircle(Pair<Double, Double> center, double radius) {
        double randomAngle = RandUtil.getRandomDouble(0, 2 * Math.PI);
        double randomRadius = RandUtil.getRandomDouble(0, radius);
        return Pair.create(
                (center.getFirst() + randomRadius * Math.cos(randomAngle)),
                (center.getSecond() + randomRadius * Math.sin(randomAngle)));
    }


    public double left() {
        return -width / 2;
    }

    public double top() {
        return bottom() + height;
    }

    public double right() {
        return width / 2;
    }

    public double bottom() {
        return state.y() + armLenght;
    }

    public double topArm1() {
        return bottom() + height / 2;
    }

    public double bottomArm1() {
        return bottom();
    }

    public double leftBottom1() {
        return left() - height / 2;
    }

    public double rightBottom2() {
        return right() + height / 2;
    }

    public double bottomArm2() {
        return state().y();
    }

    public int nFireDots(double relforce) {
        return (int) (relforce * nFireDotsMax);
    }

    public Pair<Double, Double> centerFire(double relforce) {
        return Pair.create(midX(), bottom()-radiusFire(relforce));
    }

    private static double midX() {
        return 0.0;
    }

    public double radiusFire(double relforce) {
        return relforce * radiusFireDotsMax;
    }
}
