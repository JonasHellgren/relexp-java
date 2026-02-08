package core.animation;

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
        int animationDelay
) {
}
