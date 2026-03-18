package chapters.ch11.animation;

import chapters.ch11.domain.environment.core.StateLunar;
import com.google.common.collect.Range;
import core.foundation.util.rand.RandUtil;
import lombok.Builder;
import org.apache.commons.math3.util.Pair;

import java.awt.*;
import java.util.List;
import java.util.function.DoubleSupplier;


@Builder
record LunarParams(
        StateLunar state,
        DoubleSupplier dx,
        Color colorBackground,
        List<Pair<Double, Double>> stars,
        int xmax,
        int ymax,
        int width,
        int height,
        double armAngle,
        int armLenght,
        double antennaLength,
        double xShiftCrash,
        int nFireDotsMax,
        Range<Double> radiusFireDot,
        int radiusFireDotsMax,
        List<Color> colors
) {

    public static LunarParams create(StateLunar state) {
        return create(state, false);
    }


        public static LunarParams create(StateLunar state, boolean isCrash) {
            double xShiftCrash = 0.3;

            DoubleSupplier dx = isCrash
                    ? () -> RandUtil.getRandomDouble(-xShiftCrash,xShiftCrash)
                    : () -> 0;
            return LunarParams.builder()
                .state(state).dx(dx)
                .colorBackground(Color.BLACK)
                .stars(List.of(
                        Pair.create(-5.0,5.0),
                        Pair.create(-4.0,4.7),
                        Pair.create(-4.0,3.0),
                        Pair.create(4.0,4.0),
                        Pair.create(2.0,7.0),
                        Pair.create(3.0,3.3)))
                .xmax(6).ymax(8)
                .width(2).height(2)
                .armAngle(Math.PI / 4).armLenght(1)
                .antennaLength(1.0)
                //.xShiftCrash(xShiftCrash1)
                .nFireDotsMax(1000)
                .radiusFireDot(Range.open(0.0, 4.0)).radiusFireDotsMax(1)
                .colors(List.of(Color.WHITE, new Color(150, 150, 255),Color.BLUE))
             //   .colors(List.of(Color.WHITE, Color.YELLOW,Color.RED))
                .build();
    }

    public static LunarParams empty() {
        return create(StateLunar.zeroPosAndSpeed());
    }


    public Color randomColor() {
        return colors.get(RandUtil.getRandomIntNumber(0, colors.size()));
    }

    public Pair<Double, Double> randomPosInCircle(Pair<Double, Double> center, double radius) {
        double randomAngle = RandUtil.getRandomDouble(0, 2 * Math.PI);
        double randomRadius = RandUtil.getRandomDouble(0, radius);
        return Pair.create(
                (center.getFirst() + randomRadius * Math.cos(randomAngle)),
                (center.getSecond() + randomRadius * Math.sin(randomAngle)));
    }


    public double left() {
        return -width / 2+dx.getAsDouble();
    }

    public double top() {
        return bottom() + height;
    }

    public double right() {
        return width / 2+dx.getAsDouble();
    }

    public double bottom() {
        return state.y() + armLenght;
    }

    public double topArm1() {
        return bottom() + groundMarginal();
    }

    public int groundMarginal() {
        return height / 2;
    }

    public double bottomArm1() {
        return bottom();
    }

    public double leftBottom1() {
        return left() - groundMarginal() +dx.getAsDouble();
    }

    public double rightBottom2() {
        return right() + groundMarginal() +dx.getAsDouble();
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

    public double hory() {
        return  ymax/4;
    }

    public double randXpos() {
        return RandUtil.getRandomDouble(left(), right());
    }

    public double randYPos() {
        return RandUtil.getRandomDouble(hory(), ymax);
    }

    public float randomRadiusFireDot() {
        return (float) RandUtil.getRandomDouble(
                radiusFireDot.lowerEndpoint(), radiusFireDot.upperEndpoint());
    }

    public double midx() {
        return left() + (right() - left()) / 2;
    }

    public double topAntenna() {
        return top() + antennaLength();
    }
}
