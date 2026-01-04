package chapters.ch11.domain.environment.core;

import chapters.ch11.domain.environment.param.LunarParameters;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.foundation.util.rand.RandUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StateLunar {

    public static final int NOF_DIGITS = 1;
    VariablesLunar variables;

    public static StateLunar of(double y, double spd) {
        return new StateLunar(new VariablesLunar(y, spd));
    }

    public static StateLunar randomPosAndSpeed(LunarParameters ep) {
        return of(getRandY(ep), getRandSpd(ep));
    }

    public static StateLunar randomPosAndZeroSpeed(LunarParameters ep) {
        return of(getRandY(ep), 0);
    }

    public static StateLunar zeroPosAndSpeed() {
        return of(0, 0);
    }

    public double y() {
        return variables.y();
    }

    public double spd() {
        return variables.spd();
    }

    public StateLunar copy() {
        return new StateLunar(variables);
    }

    private static double getRandSpd(LunarParameters ep) {
        return RandUtils.getRandomDouble(-ep.spdMax(), ep.spdMax());
    }

    private static double getRandY(LunarParameters ep) {
        return RandUtils.getRandomDouble(ep.ySurface(), ep.yMax());
    }

    @Override
    public String toString() {
        return "StateLunar{" +
                "y=" + NumberFormatterUtil.getRoundedNumberAsString(variables.y(), NOF_DIGITS) +
                ", spd=" + NumberFormatterUtil.getRoundedNumberAsString(variables.spd(), NOF_DIGITS) +
                '}';
    }

}
