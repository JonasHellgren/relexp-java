package core.plotting;


import core.foundation.config.ConfigReader;
import core.foundation.util.cond.ConditionalUtil;
import lombok.extern.java.Log;

import java.util.Properties;

@Log
public record PlotConfig(int width, int height, int fixedNumberForTest) {

    public static PlotConfig extract(Properties props) {
        ConditionalUtil.executeIfTrue(props == null,
                () -> log.info("properties is null, using defaults"));

        return (props == null)
                ? PlotConfig.defaults()
                : PlotConfig.extract(new ConfigReader(props));
    }

    public static PlotConfig extract(ConfigReader r) {
        return new PlotConfig(
                r.requireInt("plot.width"),
                r.requireInt("plot.height"),
                r.requireInt("fixedNumberForTest")
        );
    }

    public static PlotConfig defaults() {
        return new PlotConfig(800, 500, 0);
    }
}
