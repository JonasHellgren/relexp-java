package core.nextlevelrl.gradient;

import core.foundation.util.math.MathUtil;
import lombok.Builder;

/***
 *  Argument grad0 is potentially changed
 *  It is always bounded to [gradMin,gradMax]
 *  If motivated the bound can be more drastic
 *  If for example value is 9 and valueMax is 10, then the upper gradient bound is 1
 *  If value is -9 and valueMin is -10, then the upper gradient bound is -10-9=-1
 *  When value is below valueMin or above valueMax then gradient is modified to zero
 */

@Builder
public final class SafeGradientClipper {

    @Builder.Default
    Double valueMax=Double.MAX_VALUE;
    @Builder.Default
    Double valueMin=-Double.MAX_VALUE;
    @Builder.Default
    Double gradMax=Double.MAX_VALUE;
    @Builder.Default
    Double gradMin=-Double.MAX_VALUE;

    /**
     * This method modifies a gradient value based on the current value and the clipper's settings.
     *
     * @param originalGradient The original gradient value.
     * @param currentValue The current value.
     * @return The modified gradient value.
     */

    public final double modify(double originalGradient, double currentValue) {
        double maxValueDelta = valueMax - currentValue;
        double minValueDelta = valueMin - currentValue;
        double minGradient = (currentValue <= valueMin) ? 0 : Math.max(gradMin, minValueDelta);
        double maxGradient = (currentValue >= valueMax) ? 0 : Math.min(gradMax, maxValueDelta);
        return MathUtil.clip(originalGradient, minGradient, maxGradient);
    }
}
