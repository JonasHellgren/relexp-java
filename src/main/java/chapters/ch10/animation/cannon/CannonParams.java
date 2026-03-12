package chapters.ch10.animation.cannon;

import core.foundation.util.rand.RandUtil;
import lombok.Builder;
import org.apache.commons.math3.util.Pair;

import java.awt.Color;
import java.util.List;

@Builder
record CannonParams(
        Color colorBackground,
        int cannonWestXpos,
        int cannonWestYpos,
        int lengthCannon,
        int widthCannon,
        Color cannonColor,
        int widthTarget,
        int heightTarget,
        int nFireDots,
        List<Color> fireColors,
        int radiusFireDots,
        int radiusFireDot
) {

    public static CannonParams create() {
        return CannonParams.builder()
                .colorBackground(Color.DARK_GRAY)
                .cannonWestXpos(0)
                .cannonWestYpos(0)
                .lengthCannon(70)
                .widthCannon(5)
                .cannonColor(Color.WHITE)
                .widthTarget(30*60)
                .heightTarget(30)
                .nFireDots(20)
                .fireColors(List.of(Color.RED,Color.YELLOW))
                .radiusFireDots(20)
                .radiusFireDot(3)
                .build();
    }


    public int cannonEastXPos(double angle) {
        return (int) ((cannonWestXpos +lengthCannon)*Math.cos(angle));
    }

    public int cannonEastYPos(double angle) {
        return (int) ((cannonWestXpos +lengthCannon)*Math.sin(angle));
    }

    public Color randomColor() {
        return fireColors.get(RandUtil.getRandomIntNumber(0, fireColors.size()));
    }

    public int fireDotsCenterX(double angle) {
        return (int) (cannonEastYPos(angle)+radiusFireDots*Math.cos(angle));
    }


    public int fireDotsCenterY(double angle) {
        return (int) (cannonEastYPos(angle)+radiusFireDots*Math.sin(angle));
    }


    /*

    eastY    O
           /
         /
       /     eastX

     */

    public Pair<Integer, Integer> randomXPosFireDot(double angle) {
        double randomAngle=RandUtil.getRandomDouble(0, 2*Math.PI);
        double randomRadius=RandUtil.getRandomDouble(0, radiusFireDots);
        double dist=radiusFireDot*10.0;
        return Pair.create(
                (int) (cannonEastXPos(angle)+dist*Math.cos(angle)+randomRadius*Math.cos(randomAngle)),
                (int) (cannonEastYPos(angle)+dist*Math.sin(angle)+randomRadius*Math.sin(randomAngle)));
    }

}
