package core.foundation.config;

import core.foundation.util.cond.ConditionalsUtil;
import lombok.extern.java.Log;
import oshi.util.FormatUtil;

import java.math.BigDecimal;
import java.util.Properties;

@Log
public record AnimationConfig(
        int sleepTimeAnimationMs,
        int socketPort,
        int ndigits,
        int fontsizeAxis,
        int fontsize
) {

    public static AnimationConfig extract(Properties props) {
        ConditionalsUtil.executeIfTrue(props == null,
                () -> log.info("properties is null, using defaults"));

        return (props == null)
                ? AnimationConfig.defaults()
                : AnimationConfig.extract(new ConfigReader(props));
    }

    public static AnimationConfig extract(ConfigReader r) {
        return new AnimationConfig(
                r.requireInt("anim.sleepTimeMs"),
                r.requireInt("anim.socketPort"),
                r.requireInt("anim.ndigits"),
                r.requireInt("anim.fontsizeAxis"),
                r.requireInt("anim.fontsize")
        );
    }

    public static AnimationConfig defaults() {
        return new AnimationConfig(50,1234,2,12,12);
    }

    public float round(double value) {
        return round((float) value,ndigits);
    }

    public static float round(float d, int decimalPlace) {
        final BigDecimal bd = new BigDecimal(Float.toString(d)).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
