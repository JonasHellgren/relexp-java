package chapters.ch13.animation;

import com.google.common.base.Preconditions;
import core.foundation.gadget.pos.PosXyDouble;
import lombok.Builder;

import java.awt.Color;
import java.util.List;


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
                .xmax(10).ymin(-6).ymax(2)
                .nMidLines(10).yMidLine(-1.5)
                .lengthMidLine(1).distBetweenMidlines(2).thiknessMidlines(2).midLineColor(Color.gray)
                .carMidPos(5)
                .carLenght(4.0).carWidth(2.0).thiknessCarLine(2).carColor(Color.GREEN)
                .build();
    }

    public static LaneChangeParams empty() {
        return create();
    }

    double posLeftSingleMidline(double x, int i) {
        double remainder = x - Math.floor(x / distBetweenMidlines) * distBetweenMidlines;
        return -distBetweenMidlines + remainder + i * distBetweenMidlines;
    }

    double posRightSingleMidline(double x, int i) {
        return posLeftSingleMidline(x, i) + lengthMidLine;
    }

    public List<PosXyDouble> carCorners(double angle, double yPos) {
        return List.of(
                carCorner(angle, yPos, 0),
                carCorner(angle, yPos, 1),
                carCorner(angle, yPos, 2),
                carCorner(angle, yPos, 3));
    }

    public PosXyDouble carCorner(double angle, double yPos, int index) {
        Preconditions.checkArgument(index >= 0 && index < 4);
        return switch (index) {
            case 0 -> getXyDouble(1, -1, 1, 1, angle, yPos);
            case 1 -> getXyDouble(1, 1, 1, -1, angle, yPos);
            case 2 -> getXyDouble(-1, 1, -1, -1, angle, yPos);
            case 3 -> getXyDouble(-1, -1, -1, 1, angle, yPos);
            default -> null;
        };
    }

    /* Project the point onto the rectangle’s edge vectors (c0→c1 and c0→c3).
     If the projections lie within both edge lengths, the point is inside.
    This works because any point inside can be expressed as a combination of these two vectors*/
    public boolean isInsideRectangle(List<PosXyDouble> corners, PosXyDouble p) {
        var c0 = corners.get(0);
        var c1 = corners.get(1);
        var c3 = corners.get(3);

        double ux = c1.x() - c0.x();
        double uy = c1.y() - c0.y();

        double vx = c3.x() - c0.x();
        double vy = c3.y() - c0.y();

        double wx = p.x() - c0.x();
        double wy = p.y() - c0.y();

        double s = wx * ux + wy * uy;
        double t = wx * vx + wy * vy;
        double uu = ux * ux + uy * uy;
        double vv = vx * vx + vy * vy;
        return (0 <= s && s <= uu) && (0 <= t && t <= vv);
    }


    private PosXyDouble getXyDouble(double s1, double s2, double s3, double s4, double angle, double yPos) {
        double ldiv2 = carLenght / 2;
        double wdiv2 = carWidth / 2;
        double ca = Math.cos(angle);
        double sa = Math.sin(angle);
        return PosXyDouble.of(carMidPos + s1 * ldiv2 * ca + s2 * wdiv2 * sa, yPos + s3 * ldiv2 * sa + s4 * wdiv2 * ca);
    }

}
