package chapters.ch13.animation;

import core.foundation.gadget.pos.PosXyDouble;
import lombok.Builder;

import java.awt.*;


@Builder
record LaneChangeParams(
        Color colorBackground,
        double midx,
        double bottomy,
        int xmax,  //cm
        int ymin,  //m
        int ymax,  //m
        int nMidLines,
        double lengthMidLine,
        double distBetweenMidlines,
        double thiknessMidlines,
        double yMidLine,
        Color midLineColor,
        double carMidPos,
        double carLenght,
        double carWidth,
        double thiknessCarLine,
        Color carColor
) {


    public static LaneChangeParams create() {
        return LaneChangeParams.builder()
                .colorBackground(Color.BLACK)
                .midx(0).bottomy(0)
                .xmax(15).ymin(-6).ymax(2)
                .nMidLines(10).yMidLine(-1.5)
                .lengthMidLine(1).distBetweenMidlines(2).thiknessMidlines(2).midLineColor(Color.gray)
                .carMidPos(7)
                .carLenght(4.0).carWidth(2.0).thiknessCarLine(2).carColor(Color.WHITE)
                .build();
    }

    public static LaneChangeParams empty() {
        return create();
    }

    double posLeftSingleMidline(double x, int i) {
        double remainder = x - Math.floor(x / distBetweenMidlines) * distBetweenMidlines;
        return -distBetweenMidlines+remainder+ i * distBetweenMidlines;
    }

    double posRightSingleMidline(double x, int i) {
        return posLeftSingleMidline(x, i) + lengthMidLine;
    }

    public PosXyDouble carCorner(double angle, double yPos, int index) {
        return switch (index) {
            case 0 -> getXyDouble(1, -1, 1, 1, angle, yPos);
            case 1 -> getXyDouble(1, 1, 1, -1, angle, yPos);
            case 2 -> getXyDouble(-1, 1, -1, -1, angle, yPos);
            case 3 -> getXyDouble(-1, -1, -1, 1, angle, yPos);
            default -> null;
        };
    }

    private PosXyDouble getXyDouble(double s1,double s2, double s3, double s4,double angle,double yPos) {
        double ldiv2=carLenght/2;
        double wdiv2=carWidth/2;
        double ca=Math.cos(angle);
        double sa=Math.sin(angle);
        return PosXyDouble.of(carMidPos + s1*ldiv2 * ca + s2*wdiv2 * sa, yPos + s3*ldiv2 * sa + s4*wdiv2 * ca);
    }

}
