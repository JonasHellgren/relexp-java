package chapters.ch10.animation.cannon;
import core.foundation.util.rand.RandUtil;
import lombok.Builder;
import org.apache.commons.math3.util.Pair;
import java.awt.Color;
import java.util.List;


    /*

    eastY    O
           /
         /
       /     eastX

     */

@Builder
record CannonParams(
        Color colorBackground,
        int cannonWestXpos,
        int cannonWestYpos,
        int lengthCannon,
        int widthCannon,
        Color cannonColor,
        int distTarget,
        int widthTarget,
        int heightTarget,
        int nFireDots,
        List<Color> fireColors,
        List<Color> targetHitColors,
        int radiusFireDotsCannon,
        int radiusFireDotsHitSmall,
        int radiusFireDotsHitLarge,
        int radiusFireDot
) {

    public static CannonParams create() {
        return CannonParams.builder()
                .colorBackground(Color.WHITE)
                .cannonWestXpos(0)
                .cannonWestYpos(0)
                .lengthCannon(30)
                .widthCannon(5)
                .cannonColor(Color.BLACK)
                .distTarget(800)
                .widthTarget(60)
                .heightTarget(30)
                .nFireDots(40)
                .fireColors(List.of(Color.RED,Color.YELLOW))
                .targetHitColors(List.of(Color.BLACK,Color.RED))
                .radiusFireDotsCannon(20)
                .radiusFireDotsHitSmall(10).radiusFireDotsHitLarge(60)
                .radiusFireDot(3)
                .build();
    }


    public int cannonEastXPos(double angle) {
        return (int) ((cannonWestXpos +lengthCannon)*Math.cos(angle));
    }

    public int cannonEastYPos(double angle) {
        return (int) ((cannonWestXpos +lengthCannon)*Math.sin(angle));
    }

    public Color randomColor(List<Color> colors) {
        return colors.get(RandUtil.getRandomIntNumber(0, colors.size()));
    }


    public Pair<Integer, Integer> centerCannonFire(double angle) {
        double dist= radiusFireDotsCannon * 2 ;
        double x = cannonEastXPos(angle) + dist * Math.cos(angle);
        double y = cannonEastYPos(angle) + dist * Math.sin(angle);
        return Pair.create((int) x,(int)y);
    }

    public Pair<Integer, Integer> randomPosInCircle(Pair<Integer, Integer> center, int radius) {
        double randomAngle=RandUtil.getRandomDouble(0, 2*Math.PI);
        double randomRadius=RandUtil.getRandomDouble(0, radius);
        return Pair.create(
                (int) (center.getFirst() + randomRadius * Math.cos(randomAngle)),
                (int) (center.getSecond() + randomRadius * Math.sin(randomAngle)));
    }

    public int targetLeft(double dist) {
        return (int) (dist-widthTarget/2);
    }

    public int targetRight(double dist) {
        return (int) (dist+widthTarget/2);
    }

    public int targetTop(double dist) {
        return (int) (dist+widthTarget/2);
    }

}
