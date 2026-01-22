package chapters.ch11.domain.environment.startstate_suppliers;

import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import core.foundation.util.math.MathUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.math3.util.Pair;

@AllArgsConstructor
public class StartStateSupplierRandomAndClipped implements StartStateSupplierI   {

    LunarParameters ep;
    Pair<Double,Double> yMinMax;
    Pair<Double,Double> spdMinMax;

    public static StartStateSupplierI create(
            LunarParameters ep, Pair<Double,Double> yMinMax, Pair<Double,Double> spdMinMax) {
        return new StartStateSupplierRandomAndClipped(ep, yMinMax,spdMinMax);
    }

    @Override
    public StateLunar getStartState() {
        var state = StateLunar.randomPosAndSpeed(ep);
        double yClipped = MathUtil.clip(state.y(), yMinMax.getFirst(), yMinMax.getSecond());
        double spdClipped = MathUtil.clip(state.spd(), spdMinMax.getFirst(), spdMinMax.getSecond());
        return StateLunar.of(yClipped, spdClipped);
    }
}
