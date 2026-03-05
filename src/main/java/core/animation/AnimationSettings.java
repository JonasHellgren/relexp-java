package core.animation;

import core.foundation.config.AnimationConfig;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder
@With
public record AnimationSettings(
        int frameWidth,
        int frameHeight,
        int frameXLocation,
        int frameYLocation,
        int panelWidth,
        int panelHeight,
        int tableHeight,
        int tableWidth,
        List<Step> order,
        int margin,

        int ndigits,
        int fontsizeAxis,
        int fontsize
        ) {


    public static final int FRAME_Y_LOCATION = 200;

    public static AnimationSettings of(AnimationConfig cfg,
                                        int width,
                                        int height,
                                        int tableHeight,
                                        int frameXLocation) {
        return AnimationSettings.builder()
                .frameWidth(width).frameHeight(height)
                .frameXLocation(frameXLocation).frameYLocation(FRAME_Y_LOCATION)
                .panelWidth(width).panelHeight(height)
                .tableWidth(width).tableHeight(tableHeight)
                .order(List.of(Step.LINE, Step.HEATMAP, Step.TABLE))
                .margin(0)
                .ndigits(cfg.ndigits())
                .fontsize(cfg.fontsize())
                .fontsizeAxis(cfg.fontsizeAxis())
                .build();
    }


}
