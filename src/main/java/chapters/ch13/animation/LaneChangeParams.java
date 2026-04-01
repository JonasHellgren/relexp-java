package chapters.ch13.animation;

import chapters.ch12.domain.inv_pendulum.environment.core.EnvironmentPendulum;
import chapters.ch12.domain.inv_pendulum.trainer.core.ExperiencePendulum;
import core.foundation.gadget.pos.PosXyDouble;
import lombok.Builder;

import java.awt.*;
import java.util.Optional;


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
        double carLenght,
        double carWidth,
        Color carColor
) {


    public static LaneChangeParams create() {
        return LaneChangeParams.builder()
                .colorBackground(Color.BLACK)
                .midx(0).bottomy(0)
                .xmax(50).ymin(-4).ymax(1)

                .nMidLines(5).yMidLine(-1.5)
                .lengthMidLine(1).distBetweenMidlines(2).thiknessMidlines(2).midLineColor(Color.gray)
                .carLenght(4.0).carWidth(2.0).carColor(Color.WHITE)
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


}
