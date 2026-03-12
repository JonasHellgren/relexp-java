package chapters.ch10.animation.cannon;

import lombok.Builder;

@Builder
record CannonParams(
        int left,
        int right,
        int top,
        int bottom,
        int windowLeft,
        int windowRight,
        int windowTop,
        int windowBottom,
        int coinTop,
        int armWidth,
        int armLenght
) {

}
